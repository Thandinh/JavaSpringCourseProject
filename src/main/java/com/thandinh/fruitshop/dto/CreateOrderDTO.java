package com.thandinh.fruitshop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO để tạo order từ mobile app
 */
@Data
@NoArgsConstructor
public class CreateOrderDTO {
    
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;
    
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;
    
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
    
    private String note;
    
    @NotEmpty(message = "Giỏ hàng không được trống")
    @Valid
    private List<OrderItemDTO> items;
    
    // Inner class cho cart items
    @Data
    @NoArgsConstructor
    public static class OrderItemDTO {
        @NotNull(message = "Product ID không được để trống")
        private Long productId;
        
        @NotNull(message = "Số lượng không được để trống")
        private Integer quantity;
    }
}
