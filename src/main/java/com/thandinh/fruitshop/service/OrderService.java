package com.thandinh.fruitshop.service;

import com.thandinh.fruitshop.entity.Order;
import com.thandinh.fruitshop.enums.OrderStatus;
import com.thandinh.fruitshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Lấy tất cả đơn hàng
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Lấy đơn hàng theo ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Tạo đơn hàng mới
    public Order createOrder(Order order) {
        // Tạo mã đơn hàng tự động nếu chưa có
        if (order.getCode() == null || order.getCode().isEmpty()) {
            order.setCode(generateOrderCode());
        }
        return orderRepository.save(order);
    }

    // Tạo mã đơn hàng
    private String generateOrderCode() {
        // Format: ORD-YYYYMMDD-XXXX (VD: ORD-20260303-0001)
        String prefix = "ORD-";
        String datePart = java.time.LocalDate.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")
        );
        
        // Lấy số đơn hàng trong ngày
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
        Long todayOrderCount = orderRepository.countOrdersByDateRange(startOfDay, endOfDay);
        
        String sequencePart = String.format("%04d", todayOrderCount + 1);
        
        return prefix + datePart + "-" + sequencePart;
    }

    // Cập nhật đơn hàng
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    // Xóa đơn hàng
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // Lấy tổng doanh thu theo khoảng thời gian
    public Double getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.getTotalRevenueByDateRange(startDate, endDate);
    }

    // Đếm số đơn hàng theo khoảng thời gian
    public Long countOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.countOrdersByDateRange(startDate, endDate);
    }

    // Lấy thống kê theo trạng thái
    public Map<String, Long> getOrderStatusStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("PENDING", orderRepository.countByStatus(OrderStatus.PENDING));
        stats.put("CONFIRMED", orderRepository.countByStatus(OrderStatus.CONFIRMED));
        stats.put("SHIPPING", orderRepository.countByStatus(OrderStatus.SHIPPING));
        stats.put("COMPLETED", orderRepository.countByStatus(OrderStatus.COMPLETED));
        stats.put("CANCELLED", orderRepository.countByStatus(OrderStatus.CANCELLED));
        return stats;
    }

    // Lấy tổng doanh thu đã hoàn thành
    public Double getTotalCompletedRevenue() {
        return orderRepository.getTotalRevenueByStatus(OrderStatus.COMPLETED);
    }

    // Lấy đơn hàng gần nhất
    public List<Order> getRecentOrders(int limit) {
        List<Order> orders = orderRepository.findRecentOrders();
        if (orders.size() > limit) {
            return orders.subList(0, limit);
        }
        return orders;
    }

    // Lấy thống kê doanh thu theo tháng
    public List<Object[]> getRevenueByMonths(LocalDateTime startDate) {
        return orderRepository.getRevenueByMonths(startDate);
    }

    // Lấy thống kê theo ngày trong tháng
    public List<Object[]> getMonthlyStats(int year, int month) {
        return orderRepository.getMonthlyStats(year, month);
    }
}
