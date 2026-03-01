package com.thandinh.fruitshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    // Use different path to avoid conflict with ProductDetailController
    @GetMapping("/san-pham-list")
    public String products(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            Model model) {
        
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        if (categoryId != null) {
            model.addAttribute("categoryId", categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("keyword", keyword);
        }
        
        return "products";
    }
}
