package com.thandinh.fruitshop.service;

import com.thandinh.fruitshop.entity.Product;
import com.thandinh.fruitshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all products
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Get all products with pagination
     */
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * Find product by ID
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Find product by slug
     */
    public Product findBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    /**
     * Find products with filters and pagination
     */
    public Page<Product> findByFilters(String keyword, Long categoryId, 
                                       Double minPrice, Double maxPrice, 
                                       Pageable pageable) {
        return productRepository.findByFilters(keyword, categoryId, minPrice, maxPrice, pageable);
    }

    /**
     * Save or update product
     */
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * Delete product by ID
     */
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Check if product exists
     */
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    /**
     * Count all products
     */
    public long count() {
        return productRepository.count();
    }
}
