package com.thandinh.fruitshop.service;

import com.thandinh.fruitshop.entity.OrderItem;
import com.thandinh.fruitshop.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Lấy tất cả order items
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    // Lấy order item theo ID
    public OrderItem getOrderItemById(Long id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    // Lấy order items theo Order ID
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    // Tạo order item mới
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    // Cập nhật order item
    public OrderItem updateOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    // Xóa order item
    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }

    // Lấy top sản phẩm bán chạy
    public List<Object[]> getTopSellingProducts() {
        return orderItemRepository.getTopSellingProducts();
    }

    // Tính tổng tiền của một đơn hàng
    public Double calculateOrderTotal(Long orderId) {
        List<OrderItem> items = getOrderItemsByOrderId(orderId);
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
