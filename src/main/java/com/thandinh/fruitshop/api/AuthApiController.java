package com.thandinh.fruitshop.api;

import com.thandinh.fruitshop.dto.JwtResponseDTO;
import com.thandinh.fruitshop.dto.LoginDTO;
import com.thandinh.fruitshop.dto.RegisterDTO;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.security.JwtUtil;
import com.thandinh.fruitshop.service.AuthService;
import com.thandinh.fruitshop.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * API đăng ký - Trả về JWT token cho mobile
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            User user = authService.register(registerDTO);
            
            // Generate JWT token cho mobile
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String jwtToken = jwtUtil.generateToken(userDetails);
            
            String role = user.getRoles().stream()
                    .findFirst()
                    .map(r -> r.getName().replace("ROLE_", ""))
                    .orElse("USER");
            
            JwtResponseDTO jwtResponse = new JwtResponseDTO(
                jwtToken,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone() != null ? user.getPhone() : "",
                user.getAddress() != null ? user.getAddress() : "",
                role
            );
            
            return ResponseEntity.ok(jwtResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * API đăng nhập - Trả về JWT token cho mobile
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            User user = authService.login(loginDTO);
            
            // Generate JWT token cho mobile
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String jwtToken = jwtUtil.generateToken(userDetails);
            
            String role = user.getRoles().stream()
                    .findFirst()
                    .map(r -> r.getName().replace("ROLE_", ""))
                    .orElse("USER");
            
            JwtResponseDTO jwtResponse = new JwtResponseDTO(
                jwtToken,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone() != null ? user.getPhone() : "",
                user.getAddress() != null ? user.getAddress() : "",
                role
            );
            
            return ResponseEntity.ok(jwtResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
