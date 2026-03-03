package com.thandinh.fruitshop.dto;

import com.thandinh.fruitshop.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

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

    // Constructors
    public OrderDTO() {}

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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
