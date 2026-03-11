package com.thandinh.fruitshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT Response DTO - CHỈ dùng cho Mobile API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String role;

    public JwtResponseDTO(String token, Long id, String email, String fullName, String phone, String address, String role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }
}
