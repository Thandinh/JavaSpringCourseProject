package com.thandinh.fruitshop.controller;

import com.thandinh.fruitshop.service.CartService;
import com.thandinh.fruitshop.service.CartService.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/gio-hang")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = cartService.getCart(session);
        Double totalCart = cartService.calculateTotal(cart);
        int countCart = cartService.getCartCount(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("totalCart", totalCart);
        model.addAttribute("countCart", countCart);

        return "cart";
    }

    @PostMapping("/gio-hang")
    public String handleCart(
            @RequestParam String action,
            @RequestParam Long productId,
            @RequestParam(required = false, defaultValue = "1") int quantity,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String typeChange,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        switch (action) {
            case "create":
                cartService.addToCart(session, productId, quantity, price);
                redirectAttributes.addFlashAttribute("message", "Đã thêm sản phẩm vào giỏ hàng!");
                break;

            case "update":
                cartService.updateCartItem(session, productId, typeChange);
                break;

            case "delete":
                cartService.removeFromCart(session, productId);
                redirectAttributes.addFlashAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng!");
                break;

            case "clear":
                cartService.clearCart(session);
                redirectAttributes.addFlashAttribute("message", "Đã xóa toàn bộ giỏ hàng!");
                break;
        }

        return "redirect:/gio-hang";
    }
}
