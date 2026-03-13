package com.thandinh.fruitshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productSlug;
    private Double productPrice;
    private Double productPriceSale;
    private String productImage;
    private String categoryName;
    private Integer productQuantity;
    private LocalDateTime createdAt;
}
