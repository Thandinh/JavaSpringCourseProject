package com.thandinh.fruitshop.api;

import com.thandinh.fruitshop.dto.ProductDTO;
import com.thandinh.fruitshop.entity.Product;
import com.thandinh.fruitshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductApiController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/paged")
    public ResponseEntity<Map<String, Object>> getProductsPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String keyword) {
        
        try {
            // Adjust page to 0-indexed
            int pageNumber = Math.max(0, page - 1);
            
            // Create sort
            Sort sorting = Sort.unsorted();
            if (sort != null && !sort.isEmpty()) {
                switch (sort) {
                    case "az":
                        sorting = Sort.by("name").ascending();
                        break;
                    case "za":
                        sorting = Sort.by("name").descending();
                        break;
                    case "price-asc":
                        sorting = Sort.by("pricesale").ascending();
                        break;
                    case "price-desc":
                        sorting = Sort.by("pricesale").descending();
                        break;
                    case "newest":
                        sorting = Sort.by("id").descending();
                        break;
                    case "oldest":
                        sorting = Sort.by("id").ascending();
                        break;
                }
            }
            
            Pageable pageable = PageRequest.of(pageNumber, size, sorting);
            
            // Use service layer with filters
            Page<Product> productPage = productService.findByFilters(
                keyword != null && !keyword.trim().isEmpty() ? keyword.trim() : null,
                categoryId,
                minPrice,
                maxPrice,
                pageable
            );
            
            List<ProductDTO> productDTOs = productPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("products", productDTOs);
            response.put("currentPage", page);
            response.put("totalPages", productPage.getTotalPages());
            response.put("totalItems", productPage.getTotalElements());
            response.put("pageSize", size);
            response.put("keyword", keyword);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("products", new ArrayList<>());
            errorResponse.put("currentPage", 1);
            errorResponse.put("totalPages", 0);
            errorResponse.put("totalItems", 0);
            errorResponse.put("pageSize", size);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.findAll().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(categoryId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private ProductDTO convertToDTO(Product product) {
        // Use slug if available, otherwise use id as fallback in DTO
        String slug = product.getSlug();
        if (slug == null || slug.isEmpty()) {
            // Don't save here - just use empty string as fallback
            slug = "";
        }
        
        return new ProductDTO(
                product.getId(),
                product.getName(),
                slug,
                product.getDescription(),
                product.getPrice(),
                product.getPricesale(),
                product.getQuantity(),
                product.getFullImageUrl(),  // Use fullImageUrl to auto-normalize paths
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null
        );
    }
}
