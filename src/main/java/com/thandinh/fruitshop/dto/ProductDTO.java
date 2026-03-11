package com.thandinh.fruitshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Double price;
    private Double pricesale;
    private Integer quantity;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductDTO(Long id, String name, String slug, String description, Double price, Double pricesale, 
                     Integer quantity, String imageUrl, Long categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.pricesale = pricesale;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
