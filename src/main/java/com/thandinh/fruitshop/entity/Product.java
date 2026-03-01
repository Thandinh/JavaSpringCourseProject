package com.thandinh.fruitshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, length = 255)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Double pricesale;

    @Column(nullable = false)
    private Integer quantity;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItems;

    public Product() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        // Auto-generate slug from name
        if (name != null) {
            this.slug = generateSlug(name);
        }
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPricesale() {
        return pricesale;
    }

    public void setPricesale(Double pricesale) {
        this.pricesale = pricesale;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Get the full image URL with proper path prefix.
     * Automatically handles legacy image paths.
     */
    public String getFullImageUrl() {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        
        // If already has /uploads/ or /assets/ prefix, return as-is
        if (imageUrl.startsWith("/uploads/") || imageUrl.startsWith("/assets/")) {
            return imageUrl;
        }
        
        // If starts with "uploads/", add leading slash
        if (imageUrl.startsWith("uploads/")) {
            return "/" + imageUrl;
        }
        
        // If starts with "assets/", add leading slash
        if (imageUrl.startsWith("assets/")) {
            return "/" + imageUrl;
        }
        
        // Otherwise, assume it's a legacy image in /assets/images/
        return "/assets/images/" + imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    // Helper method to generate slug from name
    private String generateSlug(String name) {
        if (name == null) return "";
        
        String slug = name.toLowerCase();
        
        // Replace Vietnamese characters
        slug = slug.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        slug = slug.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        slug = slug.replaceAll("[ìíịỉĩ]", "i");
        slug = slug.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        slug = slug.replaceAll("[ùúụủũưừứựửữ]", "u");
        slug = slug.replaceAll("[ỳýỵỷỹ]", "y");
        slug = slug.replaceAll("đ", "d");
        
        // Remove special characters and replace spaces with hyphens
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");
        slug = slug.trim().replaceAll("\\s+", "-");
        slug = slug.replaceAll("-+", "-");
        
        return slug;
    }
}
