package com.thandinh.fruitshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/trang-chu";
    }

    @GetMapping("/trang-chu")
    public String trangChu(
            @RequestParam(required = false) String denied,
            @RequestParam(required = false) String logout,
            Model model
    ) {
        // Handle messages
        if (denied != null) {
            model.addAttribute("errorMessage", "Bạn không có quyền truy cập trang này!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Đăng xuất thành công!");
        }
        
        // Data will be loaded via API calls from frontend
        model.addAttribute("headerImages", List.of(
                "header_1.webp",
                "header_2.webp",
                "header_3.webp",
                "header_4.webp",
                "header_5.webp",
                "header_6.webp"
        ));
        return "home";
    }
}
