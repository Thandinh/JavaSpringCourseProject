package com.thandinh.fruitshop.api.admin;

import com.thandinh.fruitshop.entity.Order;
import com.thandinh.fruitshop.entity.OrderItem;
import com.thandinh.fruitshop.enums.OrderStatus;
import com.thandinh.fruitshop.service.OrderService;
import com.thandinh.fruitshop.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin Order API Controller
 * REST API endpoints for order management
 */
@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderApiController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    /**
     * Get all orders
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", orders);
            response.put("total", orders.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error loading orders: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            
            if (order == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Order not found with ID: " + id);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", order);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error loading order: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get order items by order ID
     */
    @GetMapping("/{id}/items")
    public ResponseEntity<Map<String, Object>> getOrderItems(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            
            if (order == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Order not found with ID: " + id);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(id);
            Double totalAmount = orderItemService.calculateOrderTotal(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("order", order);
            response.put("items", orderItems);
            response.put("totalAmount", totalAmount);
            response.put("totalItems", orderItems.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error loading order items: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Update order status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            Order order = orderService.getOrderById(id);
            
            if (order == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Order not found with ID: " + id);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            String statusStr = request.get("status");
            OrderStatus status = OrderStatus.valueOf(statusStr.toUpperCase());
            
            order.setStatus(status);
            Order updatedOrder = orderService.updateOrder(order);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order status updated successfully!");
            response.put("data", updatedOrder);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Invalid order status: " + request.get("status"));
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error updating order: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Delete order
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            
            if (order == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Order not found with ID: " + id);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            orderService.deleteOrder(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order deleted successfully!");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error deleting order: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get order statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getOrderStatistics() {
        try {
            Map<String, Long> statusStats = orderService.getOrderStatusStatistics();
            Double completedRevenue = orderService.getTotalCompletedRevenue();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("statusStatistics", statusStats);
            response.put("completedRevenue", completedRevenue);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error loading statistics: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
