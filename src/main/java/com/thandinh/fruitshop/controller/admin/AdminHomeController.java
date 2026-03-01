package com.thandinh.fruitshop.controller.admin;

import com.thandinh.fruitshop.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin-home")
    public String adminHome(Model model) {
        // Lấy tất cả thống kê từ service
        Map<String, Object> stats = dashboardService.getDashboardStatistics();
        
        // Thêm tất cả thống kê vào model
        model.addAllAttributes(stats);

        return "admin/dashboard";
    }
}
