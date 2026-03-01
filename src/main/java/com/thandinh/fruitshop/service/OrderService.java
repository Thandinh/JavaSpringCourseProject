package com.thandinh.fruitshop.service;

import com.thandinh.fruitshop.entity.Order;
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
        return orderRepository.save(order);
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
        stats.put("PENDING", orderRepository.countByStatus("PENDING"));
        stats.put("CONFIRMED", orderRepository.countByStatus("CONFIRMED"));
        stats.put("SHIPPING", orderRepository.countByStatus("SHIPPING"));
        stats.put("COMPLETED", orderRepository.countByStatus("COMPLETED"));
        stats.put("CANCELLED", orderRepository.countByStatus("CANCELLED"));
        return stats;
    }

    // Lấy tổng doanh thu đã hoàn thành
    public Double getTotalCompletedRevenue() {
        return orderRepository.getTotalCompletedRevenue();
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
