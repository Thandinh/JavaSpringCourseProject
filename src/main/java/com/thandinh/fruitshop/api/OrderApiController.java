package com.thandinh.fruitshop.api;

import com.thandinh.fruitshop.dto.CreateOrderDTO;
import com.thandinh.fruitshop.entity.Order;
import com.thandinh.fruitshop.entity.OrderItem;
import com.thandinh.fruitshop.entity.Product;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.enums.OrderStatus;
import com.thandinh.fruitshop.repository.ProductRepository;
import com.thandinh.fruitshop.repository.UserRepository;
import com.thandinh.fruitshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Order API Controller - Dành cho Mobile App
 */
@RestController
@RequestMapping("/api/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Tạo đơn hàng mới từ mobile
     * POST /api/orders
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Lấy user đã đăng nhập từ JWT
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập để đặt hàng!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            String email = authentication.getName();
            User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin người dùng!"));

            // Validate cart items
            if (createOrderDTO.getItems() == null || createOrderDTO.getItems().isEmpty()) {
                response.put("success", false);
                response.put("message", "Giỏ hàng trống!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Tạo đơn hàng
            Order order = new Order();
            order.setUser(user);
            order.setFullName(createOrderDTO.getFullName());
            order.setPhone(createOrderDTO.getPhone());
            order.setAddress(createOrderDTO.getAddress());
            order.setNote(createOrderDTO.getNote());
            order.setStatus(OrderStatus.PENDING);

            // Tạo order items và tính tổng tiền
            List<OrderItem> orderItems = new ArrayList<>();
            double totalAmount = 0;

            for (CreateOrderDTO.OrderItemDTO itemDTO : createOrderDTO.getItems()) {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new Exception("Không tìm thấy sản phẩm #" + itemDTO.getProductId()));

                // Kiểm tra số lượng tồn kho
                if (product.getQuantity() < itemDTO.getQuantity()) {
                    response.put("success", false);
                    response.put("message", "Sản phẩm '" + product.getName() + "' không đủ số lượng trong kho!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setPrice(product.getPrice());
                
                // Giảm số lượng sản phẩm trong kho
                product.setQuantity(product.getQuantity() - itemDTO.getQuantity());
                productRepository.save(product);
                
                orderItems.add(orderItem);
                totalAmount += product.getPrice() * itemDTO.getQuantity();
            }

            order.setTotalAmount(totalAmount);
            order.setOrderItems(orderItems);

            // Lưu đơn hàng
            Order savedOrder = orderService.createOrder(order);

            // Trả về response
            response.put("success", true);
            response.put("message", "Đặt hàng thành công!");
            response.put("order", Map.of(
                "id", savedOrder.getId(),
                "code", savedOrder.getCode(),
                "totalAmount", savedOrder.getTotalAmount(),
                "status", savedOrder.getStatus().toString(),
                "fullName", savedOrder.getFullName(),
                "phone", savedOrder.getPhone(),
                "address", savedOrder.getAddress(),
                "note", savedOrder.getNote() != null ? savedOrder.getNote() : "",
                "createdAt", savedOrder.getCreatedAt()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Lấy danh sách đơn hàng của user
     * GET /api/orders
     */
    @GetMapping
    public ResponseEntity<?> getUserOrders() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            String email = authentication.getName();
            User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                    .orElseThrow(() -> new Exception("Không tìm thấy người dùng!"));

            List<Order> orders = orderService.getOrdersByUserId(user.getId());

            List<Map<String, Object>> orderList = new ArrayList<>();
            for (Order order : orders) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", order.getId());
                orderMap.put("code", order.getCode());
                orderMap.put("totalAmount", order.getTotalAmount());
                orderMap.put("status", order.getStatus().toString());
                orderMap.put("fullName", order.getFullName());
                orderMap.put("phone", order.getPhone());
                orderMap.put("address", order.getAddress());
                orderMap.put("createdAt", order.getCreatedAt());
                orderMap.put("itemCount", order.getOrderItems() != null ? order.getOrderItems().size() : 0);
                orderList.add(orderMap);
            }

            response.put("success", true);
            response.put("orders", orderList);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Lấy chi tiết đơn hàng
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            String email = authentication.getName();
            User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                    .orElseThrow(() -> new Exception("Không tìm thấy người dùng!"));

            Order order = orderService.getOrderById(id);
            if (order == null) {
                response.put("success", false);
                response.put("message", "Không tìm thấy đơn hàng!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Kiểm tra quyền truy cập
            if (!order.getUser().getId().equals(user.getId())) {
                response.put("success", false);
                response.put("message", "Bạn không có quyền xem đơn hàng này!");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            // Tạo order details
            Map<String, Object> orderDetail = new HashMap<>();
            orderDetail.put("id", order.getId());
            orderDetail.put("code", order.getCode());
            orderDetail.put("totalAmount", order.getTotalAmount());
            orderDetail.put("status", order.getStatus().toString());
            orderDetail.put("fullName", order.getFullName());
            orderDetail.put("phone", order.getPhone());
            orderDetail.put("address", order.getAddress());
            orderDetail.put("note", order.getNote());
            orderDetail.put("createdAt", order.getCreatedAt());

            // Order items
            List<Map<String, Object>> items = new ArrayList<>();
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("id", item.getId());
                    itemMap.put("productId", item.getProduct().getId());
                    itemMap.put("productName", item.getProduct().getName());
                    itemMap.put("quantity", item.getQuantity());
                    itemMap.put("price", item.getPrice());
                    itemMap.put("subtotal", item.getPrice() * item.getQuantity());
                    itemMap.put("imageUrl", item.getProduct().getImageUrl());
                    items.add(itemMap);
                }
            }
            orderDetail.put("items", items);

            response.put("success", true);
            response.put("order", orderDetail);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
