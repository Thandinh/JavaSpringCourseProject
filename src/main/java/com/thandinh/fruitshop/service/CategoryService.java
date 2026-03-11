package com.thandinh.fruitshop.service;

import com.thandinh.fruitshop.entity.Category;
import com.thandinh.fruitshop.exception.BadRequestException;
import com.thandinh.fruitshop.exception.DuplicateResourceException;
import com.thandinh.fruitshop.exception.ResourceNotFoundException;
import com.thandinh.fruitshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get all categories
     */
    public List<Category> findAll() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    /**
     * Get category by ID
     */
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    
    /**
     * Get category by name
     */
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    /**
     * Create new category
     */
    public Category create(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new BadRequestException("Category name cannot be empty");
        }
        
        if (categoryRepository.existsByName(category.getName().trim())) {
            throw new DuplicateResourceException("Category", "name", category.getName());
        }
        
        category.setName(category.getName().trim());
        category.setCreatedAt(LocalDateTime.now());
        category.setCreatedBy("admin"); // You can get from SecurityContext
        
        return categoryRepository.save(category);
    }
    
    /**
     * Update existing category
     */
    public Category update(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        if (categoryDetails.getName() == null || categoryDetails.getName().trim().isEmpty()) {
            throw new BadRequestException("Category name cannot be empty");
        }
        
        String newName = categoryDetails.getName().trim();
        if (!category.getName().equals(newName) && categoryRepository.existsByNameAndIdNot(newName, id)) {
            throw new DuplicateResourceException("Category", "name", newName);
        }
        
        category.setName(newName);
        category.setUpdatedAt(LocalDateTime.now());
        category.setUpdatedBy("admin"); // You can get from SecurityContext
        
        return categoryRepository.save(category);
    }
    
    /**
     * Delete category by ID
     */
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        // Check if category has products
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new BadRequestException("Cannot delete category with existing products. Please reassign or delete products first.");
        }
        
        categoryRepository.deleteById(id);
    }
    
    /**
     * Check if category exists by name
     */
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
    
    /**
     * Get total count of categories
     */
    public long count() {
        return categoryRepository.count();
    }
}
