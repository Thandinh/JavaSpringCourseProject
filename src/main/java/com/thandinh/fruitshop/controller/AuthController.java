package com.thandinh.fruitshop.controller;

import com.thandinh.fruitshop.dto.RegisterDTO;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Hiển thị trang đăng ký
     */
    @GetMapping("/dang-ky")
    public String showRegisterPage(Model model) {
        // Nếu đã đăng nhập, redirect về trang chủ
        if (isAuthenticated()) {
            return "redirect:/trang-chu";
        }
        
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    /**
     * Xử lý đăng ký
     */
    @PostMapping("/dang-ky")
    public String register(
            @Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Nếu đã đăng nhập, redirect về trang chủ
        if (isAuthenticated()) {
            return "redirect:/trang-chu";
        }
        
        // Kiểm tra validation errors
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            // Đăng ký user
            User user = authService.register(registerDTO);
            
            // Thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage", 
                "Đăng ký thành công! Vui lòng đăng nhập.");
            
            return "redirect:/dang-nhap";
            
        } catch (Exception e) {
            model.addAttribute("errorEmail", e.getMessage());
            return "register";
        }
    }

    /**
     * Hiển thị trang đăng nhập
     * Spring Security sẽ xử lý POST request
     */
    @GetMapping("/dang-nhap")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model
    ) {
        // Nếu đã đăng nhập, redirect về trang chủ
        if (isAuthenticated()) {
            return "redirect:/trang-chu";
        }
        
        if (error != null) {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Đăng xuất thành công!");
        }
        return "login";
    }
    
    /**
     * Kiểm tra user đã đăng nhập chưa
     */
    private boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
    }
}
