package com.thandinh.fruitshop.service;

import com.thandinh.fruitshop.entity.Order;
import com.thandinh.fruitshop.repository.OrderItemRepository;
import com.thandinh.fruitshop.repository.ProductRepository;
import com.thandinh.fruitshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * Lấy tất cả thống kê cho dashboard
     */
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();

        LocalDate today = LocalDate.now();
        
        // Thống kê hôm nay
        stats.put("todayRevenue", getTodayRevenue(today));
        stats.put("todayOrders", getTodayOrders(today));

        // Thống kê tháng này
        stats.put("thisMonthRevenue", getThisMonthRevenue(today));
        stats.put("thisMonthOrders", getThisMonthOrders(today));

        // Tổng quan
        stats.put("totalOrders", orderService.getAllOrders().size());
        stats.put("totalUsers", userRepository.count());
        stats.put("totalProducts", productRepository.count());
        stats.put("totalRevenue", orderService.getTotalCompletedRevenue());

        // Thống kê theo trạng thái
        Map<String, Long> statusStats = orderService.getOrderStatusStatistics();
        stats.put("pendingOrders", statusStats.get("PENDING"));
        stats.put("confirmedOrders", statusStats.get("CONFIRMED"));
        stats.put("shippingOrders", statusStats.get("SHIPPING"));
        stats.put("completedOrders", statusStats.get("COMPLETED"));
        stats.put("cancelledOrders", statusStats.get("CANCELLED"));

        // Doanh thu 6 tháng gần nhất
        stats.put("monthlyRevenue", getMonthlyRevenueFormatted(today));

        // Top sản phẩm bán chạy
        stats.put("topProducts", orderItemRepository.getTopSellingProducts());

        // Đơn hàng gần nhất
        stats.put("recentOrders", orderService.getRecentOrders(10));

        // Thống kê theo ngày trong tháng hiện tại
        stats.put("statsList", orderService.getMonthlyStats(today.getYear(), today.getMonthValue()));
        stats.put("currentMonth", today.getMonthValue());
        stats.put("currentYear", today.getYear());

        return stats;
    }

    /**
     * Lấy doanh thu hôm nay
     */
    private Double getTodayRevenue(LocalDate today) {
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX);
        Double revenue = orderService.getTotalRevenueByDateRange(startOfToday, endOfToday);
        return revenue != null ? revenue : 0.0;
    }

    /**
     * Lấy số đơn hàng hôm nay
     */
    private Long getTodayOrders(LocalDate today) {
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX);
        Long orders = orderService.countOrdersByDateRange(startOfToday, endOfToday);
        return orders != null ? orders : 0;
    }

    /**
     * Lấy doanh thu tháng này
     */
    private Double getThisMonthRevenue(LocalDate today) {
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
        LocalDateTime endOfMonth = lastDayOfMonth.atTime(LocalTime.MAX);
        Double revenue = orderService.getTotalRevenueByDateRange(startOfMonth, endOfMonth);
        return revenue != null ? revenue : 0.0;
    }

    /**
     * Lấy số đơn hàng tháng này
     */
    private Long getThisMonthOrders(LocalDate today) {
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
        LocalDateTime endOfMonth = lastDayOfMonth.atTime(LocalTime.MAX);
        Long orders = orderService.countOrdersByDateRange(startOfMonth, endOfMonth);
        return orders != null ? orders : 0;
    }

    /**
     * Lấy doanh thu 6 tháng gần nhất đã format
     */
    private List<Map<String, Object>> getMonthlyRevenueFormatted(LocalDate today) {
        LocalDateTime sixMonthsAgo = today.minusMonths(6).atStartOfDay();
        List<Object[]> rawData = orderService.getRevenueByMonths(sixMonthsAgo);
        
        List<Map<String, Object>> formattedData = new ArrayList<>();
        for (Object[] row : rawData) {
            Map<String, Object> item = new HashMap<>();
            Integer year = (Integer) row[0];
            Integer month = (Integer) row[1];
            Long count = (Long) row[2];
            Double revenue = (Double) row[3];
            
            // Format month as "YYYY-MM"
            String monthLabel = String.format("%d-%02d", year, month);
            
            item.put("month", monthLabel);
            item.put("count", count);
            item.put("revenue", revenue);
            
            formattedData.add(item);
        }
        
        return formattedData;
    }
}
