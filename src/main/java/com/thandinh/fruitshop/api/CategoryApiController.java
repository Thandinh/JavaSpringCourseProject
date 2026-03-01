package com.thandinh.fruitshop.api;

import com.thandinh.fruitshop.dto.CategoryDTO;
import com.thandinh.fruitshop.entity.Category;
import com.thandinh.fruitshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryApiController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}
