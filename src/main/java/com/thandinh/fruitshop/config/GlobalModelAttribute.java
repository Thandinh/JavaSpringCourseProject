package com.thandinh.fruitshop.config;

import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.repository.UserRepository;
import com.thandinh.fruitshop.service.CartService;
import com.thandinh.fruitshop.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttribute {

    private final CategoryService categoryService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserRepository userRepository;

    public GlobalModelAttribute(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute
    public void commonData(Model model, HttpSession session) {
        // Category list
        model.addAttribute("categoryList", categoryService.findAll());
        
        // Get authenticated user from Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() 
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userFullName", user.getFullName());
            }
        }
        
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
