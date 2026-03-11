package com.thandinh.fruitshop.repository;

import com.thandinh.fruitshop.entity.Order;
import com.thandinh.fruitshop.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Tìm tất cả đơn hàng của user theo userId
    List<Order> findByUser_IdOrderByOrderDateDesc(Long userId);

    // Tính tổng doanh thu trong khoảng thời gian
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0.0) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Đếm số đơn hàng trong khoảng thời gian
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Long countOrdersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Lấy thống kê theo ngày trong tháng (ngày, số đơn hàng, doanh thu)
    @Query("SELECT DAY(o.orderDate), COUNT(o), COALESCE(SUM(o.totalAmount), 0.0) " +
           "FROM Order o " +
           "WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month " +
           "GROUP BY DAY(o.orderDate) " +
           "ORDER BY DAY(o.orderDate)")
    List<Object[]> getMonthlyStats(@Param("year") int year, @Param("month") int month);

    // Đếm số đơn hàng theo trạng thái
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(@Param("status") OrderStatus status);

    // Lấy tổng doanh thu đã hoàn thành
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0.0) FROM Order o WHERE o.status = :status")
    Double getTotalRevenueByStatus(@Param("status") OrderStatus status);

    // Lấy danh sách đơn hàng gần nhất
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders();

    // Lấy thống kê doanh thu 6 tháng gần nhất (trả về year, month, count, revenue)
    @Query("SELECT YEAR(o.orderDate), MONTH(o.orderDate), " +
           "COUNT(o), " +
           "COALESCE(SUM(CASE WHEN o.status = 'COMPLETED' THEN o.totalAmount ELSE 0 END), 0.0) " +
           "FROM Order o " +
           "WHERE o.orderDate >= :startDate " +
           "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
           "ORDER BY YEAR(o.orderDate) DESC, MONTH(o.orderDate) DESC")
    List<Object[]> getRevenueByMonths(@Param("startDate") LocalDateTime startDate);
}
