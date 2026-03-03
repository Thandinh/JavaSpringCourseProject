package com.thandinh.fruitshop.repository;

import com.thandinh.fruitshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Lấy danh sách order items theo order ID
    List<OrderItem> findByOrderId(Long orderId);

    // Lấy top sản phẩm bán chạy nhất
    @Query("SELECT p.name, SUM(oi.quantity), SUM(oi.quantity * oi.price) " +
           "FROM OrderItem oi " +
           "JOIN oi.product p " +
           "JOIN oi.order o " +
           "WHERE o.status = 'COMPLETED' " +
           "GROUP BY p.id, p.name " +
           "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> getTopSellingProducts();
}
