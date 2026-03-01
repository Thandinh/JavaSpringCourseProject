package com.thandinh.fruitshop.config;

import com.thandinh.fruitshop.service.CartService;
import com.thandinh.fruitshop.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttribute {

    private final CategoryService categoryService;
    
    @Autowired
    private CartService cartService;

    public GlobalModelAttribute(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute
    public void commonData(Model model, HttpSession session) {
        // Category list
        model.addAttribute("categoryList", categoryService.findAll());
        
        // Cart data - always recalculate to ensure accuracy
        List<CartService.CartItem> cart = cartService.getCart(session);
        int countCart = cartService.getCartCount(cart);
        Double totalCart = cartService.calculateTotal(cart);
        
        // Update session with accurate values
        session.setAttribute("cart", cart);
        session.setAttribute("countCart", countCart);
        session.setAttribute("totalCart", totalCart);
        
        // Add to model
        model.addAttribute("cart", cart);
        model.addAttribute("countCart", countCart);
        model.addAttribute("totalCart", totalCart);
    }
}
