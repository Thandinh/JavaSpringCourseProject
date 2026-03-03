package com.thandinh.fruitshop.dto;

public class OrderItemDTO {
    
    private Long id;
    private Integer quantity;
    private Double price;
    private Long orderId;
    private String orderCode;
    private Long productId;
    private String productName;
    private String productImage;
    private Double subtotal;

    // Constructors
    public OrderItemDTO() {}

    public OrderItemDTO(Long id, Integer quantity, Double price, Long orderId, 
                       String orderCode, Long productId, String productName, 
                       String productImage) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.subtotal = price * quantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
        calculateSubtotal();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    // Helper method to calculate subtotal
    private void calculateSubtotal() {
        if (this.price != null && this.quantity != null) {
            this.subtotal = this.price * this.quantity;
        }
    }
}
