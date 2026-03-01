package com.thandinh.fruitshop.api.admin;

import com.thandinh.fruitshop.dto.ProductDTO;
import com.thandinh.fruitshop.entity.Category;
import com.thandinh.fruitshop.entity.Product;
import com.thandinh.fruitshop.service.CategoryService;
import com.thandinh.fruitshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST API Controller for Admin Product Management
 * Provides JSON endpoints for CRUD operations on products
 */
@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "*")
public class AdminProductApiController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private static final String UPLOAD_DIR = "uploads/products/";

    /**
     * Get all products
     * GET /api/admin/products
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        try {
            List<Product> products = productService.findAll();
            List<ProductDTO> productDTOs = products.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Products retrieved successfully");
            response.put("data", productDTOs);
            response.put("total", productDTOs.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error retrieving products", e);
        }
    }

    /**
     * Get product by ID
     * GET /api/admin/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> productOpt = productService.findById(id);
            
            if (productOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Product not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product retrieved successfully");
            response.put("data", convertToDTO(productOpt.get()));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error retrieving product", e);
        }
    }

    /**
     * Create new product
     * POST /api/admin/products
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("pricesale") Double pricesale,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) {
        
        try {
            // Validate required fields
            if (name == null || name.trim().isEmpty()) {
                return createErrorResponse("Product name is required", HttpStatus.BAD_REQUEST);
            }
            
            if (price == null || price <= 0) {
                return createErrorResponse("Valid price is required", HttpStatus.BAD_REQUEST);
            }
            
            if (pricesale == null || pricesale <= 0) {
                return createErrorResponse("Valid sale price is required", HttpStatus.BAD_REQUEST);
            }
            
            if (quantity == null || quantity < 0) {
                return createErrorResponse("Valid quantity is required", HttpStatus.BAD_REQUEST);
            }
            
            // Create product
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setPricesale(pricesale);
            product.setQuantity(quantity);

            // Set category
            Optional<Category> category = categoryService.findById(categoryId);
            if (category.isEmpty()) {
                return createErrorResponse("Invalid category ID: " + categoryId, HttpStatus.BAD_REQUEST);
            }
            product.setCategory(category.get());

            // Handle file upload
            if (thumbnail != null && !thumbnail.isEmpty()) {
                String imageUrl = saveUploadedFile(thumbnail);
                product.setImageUrl(imageUrl);
            }

            // Save product
            Product savedProduct = productService.save(product);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product created successfully");
            response.put("data", convertToDTO(savedProduct));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            return handleError("Error creating product", e);
        }
    }

    /**
     * Update existing product
     * PUT /api/admin/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("pricesale") Double pricesale,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "oldImageUrl", required = false) String oldImageUrl) {
        
        try {
            // Find existing product
            Optional<Product> productOpt = productService.findById(id);
            if (productOpt.isEmpty()) {
                return createErrorResponse("Product not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
            
            Product product = productOpt.get();
            
            // Update fields
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setPricesale(pricesale);
            product.setQuantity(quantity);

            // Set category
            Optional<Category> category = categoryService.findById(categoryId);
            if (category.isEmpty()) {
                return createErrorResponse("Invalid category ID: " + categoryId, HttpStatus.BAD_REQUEST);
            }
            product.setCategory(category.get());

            // Handle file upload
            if (thumbnail != null && !thumbnail.isEmpty()) {
                // Delete old image if exists
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    deleteOldFile(oldImageUrl);
                }
                String imageUrl = saveUploadedFile(thumbnail);
                product.setImageUrl(imageUrl);
            }

            // Save product
            Product updatedProduct = productService.save(product);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product updated successfully");
            response.put("data", convertToDTO(updatedProduct));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return handleError("Error updating product", e);
        }
    }

    /**
     * Delete product
     * DELETE /api/admin/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        try {
            Optional<Product> productOpt = productService.findById(id);
            
            if (productOpt.isEmpty()) {
                return createErrorResponse("Product not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
            
            Product product = productOpt.get();
            
            // Check if product is in any orders
            if (product.getOrderItems() != null && !product.getOrderItems().isEmpty()) {
                return createErrorResponse("Không thể xóa sản phẩm này vì đã có trong đơn hàng. Vui lòng ngừng kinh doanh thay vì xóa.", HttpStatus.CONFLICT);
            }
            
            // Delete image file if exists
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                deleteOldFile(product.getImageUrl());
            }
            
            // Delete product
            productService.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product deleted successfully");
            response.put("data", null);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error deleting product ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return handleError("Error deleting product: " + e.getMessage(), e);
        }
    }

    /**
     * Get all categories (helper endpoint)
     * GET /api/admin/products/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        try {
            List<Category> categories = categoryService.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Categories retrieved successfully");
            response.put("data", categories);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error retrieving categories", e);
        }
    }

    /**
     * Bulk delete products
     * DELETE /api/admin/products/bulk
     */
    @DeleteMapping("/bulk")
    public ResponseEntity<Map<String, Object>> bulkDeleteProducts(@RequestBody List<Long> productIds) {
        try {
            int deletedCount = 0;
            List<String> errors = new ArrayList<>();
            
            for (Long id : productIds) {
                try {
                    Optional<Product> productOpt = productService.findById(id);
                    if (productOpt.isPresent()) {
                        Product product = productOpt.get();
                        if (product.getImageUrl() != null) {
                            deleteOldFile(product.getImageUrl());
                        }
                        productService.deleteById(id);
                        deletedCount++;
                    }
                } catch (Exception e) {
                    errors.add("Failed to delete product ID " + id + ": " + e.getMessage());
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", errors.isEmpty());
            response.put("message", deletedCount + " products deleted successfully");
            response.put("deletedCount", deletedCount);
            response.put("errors", errors);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error in bulk delete", e);
        }
    }

    /**
     * Get product statistics
     * GET /api/admin/products/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getProductStatistics() {
        try {
            List<Product> products = productService.findAll();
            
            long totalProducts = products.size();
            long inStockProducts = products.stream().filter(p -> p.getQuantity() > 0).count();
            long outOfStockProducts = products.stream().filter(p -> p.getQuantity() == 0).count();
            
            double totalValue = products.stream()
                    .mapToDouble(p -> p.getPricesale() * p.getQuantity())
                    .sum();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalProducts", totalProducts);
            stats.put("inStockProducts", inStockProducts);
            stats.put("outOfStockProducts", outOfStockProducts);
            stats.put("totalInventoryValue", totalValue);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Statistics retrieved successfully");
            response.put("data", stats);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error retrieving statistics", e);
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Convert Product entity to DTO
     */
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setPricesale(product.getPricesale());
        dto.setQuantity(product.getQuantity());
        dto.setImageUrl(product.getImageUrl());
        
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        
        return dto;
    }

    /**
     * Save uploaded file to server
     */
    private String saveUploadedFile(MultipartFile file) throws IOException {
        // Create upload directory if not exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return relative URL
        return "/uploads/products/" + uniqueFilename;
    }

    /**
     * Delete old file from server
     */
    private void deleteOldFile(String imageUrl) {
        try {
            if (imageUrl != null && imageUrl.startsWith("/uploads/products/")) {
                String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                Path filePath = Paths.get(UPLOAD_DIR + filename);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            System.err.println("Error deleting old file: " + e.getMessage());
        }
    }

    /**
     * Create error response
     */
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("data", null);
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Handle exceptions and create error response
     */
    private ResponseEntity<Map<String, Object>> handleError(String message, Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message + ": " + e.getMessage());
        response.put("error", e.getClass().getSimpleName());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
