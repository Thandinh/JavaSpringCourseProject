package com.thandinh.fruitshop.controller.admin;

import com.thandinh.fruitshop.entity.Order;
import com.thandinh.fruitshop.entity.OrderItem;
import com.thandinh.fruitshop.enums.OrderStatus;
import com.thandinh.fruitshop.service.OrderService;
import com.thandinh.fruitshop.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Admin Order Controller
 * Handles order management for admin users
 */
@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    /**
     * Display list of all orders
     */
    @GetMapping
    public String index(Model model) {
        try {
            List<Order> orderList = orderService.getAllOrders();
            model.addAttribute("orderList", orderList);
            model.addAttribute("totalOrders", orderList.size());
            return "admin/order/index";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading orders: " + e.getMessage());
            return "admin/order/index";
        }
    }

    /**
     * Show edit order form
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.getOrderById(id);
            
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Order not found with ID: " + id);
                return "redirect:/admin/orders";
            }
            
            model.addAttribute("order", order);
            model.addAttribute("orderStatuses", OrderStatus.values());
            return "admin/order/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading order: " + e.getMessage());
            return "redirect:/admin/orders";
        }
    }

    /**
     * Update order status
     */
    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable Long id, 
                              @RequestParam("status") OrderStatus status,
                              RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.getOrderById(id);
            
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Order not found with ID: " + id);
                return "redirect:/admin/orders";
            }
            
            order.setStatus(status);
            orderService.updateOrder(order);
            
            redirectAttributes.addFlashAttribute("success", "Order status updated successfully!");
            return "redirect:/admin/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating order: " + e.getMessage());
            return "redirect:/admin/orders/edit/" + id;
        }
    }

    /**
     * View order details (order items)
     */
    @GetMapping("/details/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.getOrderById(id);
            
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Order not found with ID: " + id);
                return "redirect:/admin/orders";
            }
            
            List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(id);
            Double totalAmount = orderItemService.calculateOrderTotal(id);
            
            model.addAttribute("order", order);
            model.addAttribute("orderItems", orderItems);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("code", "ORD-" + String.format("%06d", order.getId()));
            
            return "admin/orderitem/index";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading order details: " + e.getMessage());
            return "redirect:/admin/orders";
        }
    }

    /**
     * Delete order
     */
    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.getOrderById(id);
            
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Order not found with ID: " + id);
                return "redirect:/admin/orders";
            }
            
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("success", "Order deleted successfully!");
            return "redirect:/admin/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting order: " + e.getMessage());
            return "redirect:/admin/orders";
        }
    }
}
