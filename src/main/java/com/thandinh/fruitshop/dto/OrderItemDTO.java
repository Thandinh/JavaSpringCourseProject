package com.thandinh.fruitshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    // Custom setters để tự động calculate subtotal
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public void setPrice(Double price) {
        this.price = price;
        calculateSubtotal();
    }

    // Helper method to calculate subtotal
    private void calculateSubtotal() {
        if (this.price != null && this.quantity != null) {
            this.subtotal = this.price * this.quantity;
        }
    }
}
