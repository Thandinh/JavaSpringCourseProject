package com.thandinh.fruitshop.controller;

import com.thandinh.fruitshop.entity.Product;
import com.thandinh.fruitshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ProductDetailController {

    @Autowired
    private ProductService productService;

    @GetMapping("/san-pham/{slug}")
    public String productDetail(@PathVariable String slug, Model model) {
        if (slug == null || slug.isEmpty()) {
            return "redirect:/san-pham-list";
        }
        
        Product product = productService.findBySlug(slug);
        
        if (product == null) {
            return "redirect:/san-pham-list";
        }
        
        model.addAttribute("product", product);
        return "product";
    }
    
    @GetMapping("/san-pham")
    public String productDetailById(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            return "redirect:/san-pham-list";
        }
        
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isEmpty()) {
            return "redirect:/san-pham-list";
        }
        
        Product product = productOpt.get();
        model.addAttribute("product", product);
        return "product";
    }
}
