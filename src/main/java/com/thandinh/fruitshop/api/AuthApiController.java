package com.thandinh.fruitshop.api;

import com.thandinh.fruitshop.dto.LoginDTO;
import com.thandinh.fruitshop.dto.RegisterDTO;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private AuthService authService;

    /**
     * API đăng ký
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterDTO registerDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = authService.register(registerDTO);
            
            response.put("success", true);
            response.put("message", "Đăng ký thành công!");
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "fullName", user.getFullName()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * API đăng nhập
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = authService.login(loginDTO);
            
            response.put("success", true);
            response.put("message", "Đăng nhập thành công!");
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "fullName", user.getFullName(),
                "phone", user.getPhone() != null ? user.getPhone() : "",
                "address", user.getAddress() != null ? user.getAddress() : ""
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * API kiểm tra email có tồn tại không
     * GET /api/auth/check-email?email=xxx@gmail.com
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        
        boolean exists = authService.isEmailExists(email);
        boolean isGmail = authService.isGmail(email);
        
        response.put("exists", exists);
        response.put("isGmail", isGmail);
        
        if (!isGmail && !email.isEmpty()) {
            response.put("message", "Vui lòng sử dụng địa chỉ Gmail");
        } else if (exists) {
            response.put("message", "Email này đã được đăng ký");
        }
        
        return ResponseEntity.ok(response);
    }
}
