package com.thandinh.fruitshop.repository;

import com.thandinh.fruitshop.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    
    List<ProductReview> findByProductIdOrderByCreatedAtDesc(Long productId);
    
    Optional<ProductReview> findByProductIdAndUserId(Long productId, Long userId);
    
    boolean existsByProductIdAndUserId(Long productId, Long userId);
    
    @Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.product.id = :productId")
    Double getAverageRating(@Param("productId") Long productId);
    
    @Query("SELECT COUNT(r) FROM ProductReview r WHERE r.product.id = :productId")
    Long getReviewCount(@Param("productId") Long productId);
}
