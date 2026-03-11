package com.thandinh.fruitshop.dto;

import com.thandinh.fruitshop.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    
    private Long id;
    private String code;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private OrderStatus status;
    private String fullName;
    private String phone;
    private String address;
    private String note;
    private String userEmail;
    private Long userId;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Long id, String code, LocalDateTime orderDate, Double totalAmount, 
                   OrderStatus status, String fullName, String phone, String address, 
                   String note, String userEmail, Long userId) {
        this.id = id;
        this.code = code;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.userEmail = userEmail;
        this.userId = userId;
    }
}
