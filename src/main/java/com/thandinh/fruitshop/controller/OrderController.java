package com.thandinh.fruitshop.controller;

import com.thandinh.fruitshop.entity.Order;
import com.thandinh.fruitshop.entity.OrderItem;
import com.thandinh.fruitshop.entity.Product;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.enums.OrderStatus;
import com.thandinh.fruitshop.repository.ProductRepository;
import com.thandinh.fruitshop.repository.UserRepository;
import com.thandinh.fruitshop.service.CartService;
import com.thandinh.fruitshop.service.CartService.CartItem;
import com.thandinh.fruitshop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Hiển thị trang checkout/đặt hàng
     */
    @GetMapping("/dat-hang")
    public String showCheckoutPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Lấy giỏ hàng
        List<CartItem> cart = cartService.getCart(session);
        
        // Kiểm tra giỏ hàng có rỗng không
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn đang trống!");
            return "redirect:/gio-hang";
        }

        // Lấy thông tin user đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để tiếp tục!");
            return "redirect:/dang-nhap";
        }

        // Tính tổng tiền
        Double totalCart = cartService.calculateTotal(cart);
        int countCart = cartService.getCartCount(cart);

        // Add to model
        model.addAttribute("cart", cart);
        model.addAttribute("totalCart", totalCart);
        model.addAttribute("countCart", countCart);
        model.addAttribute("user", user);

        return "checkout";
    }

    /**
     * Xử lý đặt hàng
     */
    @PostMapping("/dat-hang")
    public String processCheckout(
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam(required = false) String note,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Lấy giỏ hàng
            List<CartItem> cart = cartService.getCart(session);
            
            if (cart == null || cart.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn đang trống!");
                return "redirect:/gio-hang";
            }

            // Lấy user đã đăng nhập
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để tiếp tục!");
                return "redirect:/dang-nhap";
            }

            // Tạo đơn hàng
            Order order = new Order();
            order.setUser(user);
            order.setFullName(fullName);
            order.setPhone(phone);
            order.setAddress(address);
            order.setNote(note);
            order.setStatus(OrderStatus.PENDING);
            
            // Tính tổng tiền
            Double totalAmount = cartService.calculateTotal(cart);
            order.setTotalAmount(totalAmount);

            // Lưu đơn hàng trước để có ID
            Order savedOrder = orderService.createOrder(order);

            // Tạo order items sau khi order đã có ID
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cart) {
                Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
                if (product != null) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);  // Dùng savedOrder đã có ID
                    orderItem.setProduct(product);
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItems.add(orderItem);
                }
            }
            
            // Set và lưu lại order với orderItems
            savedOrder.setOrderItems(orderItems);
            orderService.updateOrder(savedOrder);

            // Xóa giỏ hàng
            cartService.clearCart(session);

            // Thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage", 
                "Đặt hàng thành công! Mã đơn hàng: #" + savedOrder.getId());
            
            return "redirect:/trang-chu";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/dat-hang";
        }
    }
}
