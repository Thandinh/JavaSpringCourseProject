package com.thandinh.fruitshop.repository;

import com.thandinh.fruitshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findBySlug(String slug);

    @Query("SELECT p FROM Product p WHERE " +
           "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "(p.description IS NOT NULL AND LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:minPrice IS NULL OR p.pricesale >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.pricesale <= :maxPrice)")
    Page<Product> findByFilters(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}
