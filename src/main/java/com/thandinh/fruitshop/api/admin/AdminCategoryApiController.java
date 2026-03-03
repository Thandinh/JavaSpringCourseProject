package com.thandinh.fruitshop.api.admin;

import com.thandinh.fruitshop.dto.CategoryDTO;
import com.thandinh.fruitshop.entity.Category;
import com.thandinh.fruitshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST API Controller for Admin Category Management
 * Provides JSON endpoints for CRUD operations on categories
 */
@RestController
@RequestMapping("/api/admin/categories")
@CrossOrigin(origins = "*")
public class AdminCategoryApiController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Get all categories
     * GET /api/admin/categories
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        try {
            List<Category> categories = categoryService.findAll();
            List<CategoryDTO> categoryDTOs = categories.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Categories retrieved successfully");
            response.put("data", categoryDTOs);
            response.put("total", categoryDTOs.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error retrieving categories", e);
        }
    }

    /**
     * Get category by ID
     * GET /api/admin/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable Long id) {
        try {
            Optional<Category> categoryOpt = categoryService.findById(id);
            
            if (categoryOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Category not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category retrieved successfully");
            response.put("data", convertToDTO(categoryOpt.get()));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error retrieving category", e);
        }
    }

    /**
     * Create new category
     * POST /api/admin/categories
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            Category category = new Category();
            category.setName(categoryDTO.getName());
            
            Category savedCategory = categoryService.create(category);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category created successfully");
            response.put("data", convertToDTO(savedCategory));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            return handleError("Error creating category", e);
        }
    }

    /**
     * Update existing category
     * PUT /api/admin/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable Long id, 
                                                              @RequestBody CategoryDTO categoryDTO) {
        try {
            Category categoryDetails = new Category();
            categoryDetails.setName(categoryDTO.getName());
            
            Category updatedCategory = categoryService.update(id, categoryDetails);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category updated successfully");
            response.put("data", convertToDTO(updatedCategory));
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            HttpStatus status = e.getMessage().contains("not found") ? 
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            return handleError("Error updating category", e);
        }
    }

    /**
     * Delete category
     * DELETE /api/admin/categories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.delete(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            HttpStatus status = e.getMessage().contains("not found") ? 
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            return handleError("Error deleting category", e);
        }
    }

    /**
     * Get category count
     * GET /api/admin/categories/count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCategoryCount() {
        try {
            long count = categoryService.count();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category count retrieved successfully");
            response.put("count", count);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error getting category count", e);
        }
    }

    /**
     * Check if category name exists
     * GET /api/admin/categories/exists?name={name}
     */
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkCategoryExists(@RequestParam String name) {
        try {
            boolean exists = categoryService.existsByName(name);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("exists", exists);
            response.put("message", exists ? "Category name already exists" : "Category name is available");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error checking category existence", e);
        }
    }

    /**
     * Convert Category entity to DTO
     */
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    /**
     * Handle errors and return error response
     */
    private ResponseEntity<Map<String, Object>> handleError(String message, Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
