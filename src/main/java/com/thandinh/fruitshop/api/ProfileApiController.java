package com.thandinh.fruitshop.api;

import com.thandinh.fruitshop.dto.UserDTO;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ProfileApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get current user profile
     */
    @GetMapping
    public ResponseEntity<?> getProfile() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Unauthorized"));
            }

            String email = auth.getName();
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserDTO userDTO = convertToDTO(user);
            return ResponseEntity.ok(userDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update profile information
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> request, HttpSession session) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update fields
            if (request.containsKey("fullName")) {
                user.setFullName(request.get("fullName"));
            }
            if (request.containsKey("phone")) {
                user.setPhone(request.get("phone"));
            }
            if (request.containsKey("address")) {
                user.setAddress(request.get("address"));
            }

            userService.save(user);
            
            // Update session
            session.setAttribute("user", user);

            return ResponseEntity.ok(Map.of(
                    "message", "Profile updated successfully",
                    "user", convertToDTO(user)
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Change password
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        try {
            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");
            String confirmPassword = request.get("confirmPassword");

            // Validate
            if (newPassword == null || newPassword.length() < 6) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "New password must be at least 6 characters"));
            }

            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Passwords do not match"));
            }

            // Get current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check current password
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Current password is incorrect"));
            }

            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.save(user);

            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Upload avatar
     */
    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("avatar") MultipartFile avatar,
            HttpSession session) {
        
        try {
            if (avatar.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Avatar file is required"));
            }

            // Get current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Save avatar
            String avatarUrl = saveUploadedFile(avatar);
            user.setAvatar(avatarUrl);
            userService.save(user);
            
            // Update session
            session.setAttribute("user", user);

            return ResponseEntity.ok(Map.of(
                    "message", "Avatar updated successfully",
                    "avatarUrl", avatarUrl,
                    "user", convertToDTO(user)
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload avatar: " + e.getMessage()));
        }
    }

    /**
     * Save uploaded file to both src and target directories
     */
    private String saveUploadedFile(MultipartFile file) throws Exception {
        try {
            if (file.isEmpty()) {
                throw new Exception("File is empty");
            }

            // Get original filename and extension
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // Generate unique filename
            String filename = UUID.randomUUID().toString() + extension;

            // Read file content once
            byte[] fileBytes = file.getBytes();

            // Save to SOURCE directory
            Path srcUploadDir = Paths.get("src/main/resources/static/uploads/users");
            if (!Files.exists(srcUploadDir)) {
                Files.createDirectories(srcUploadDir);
            }
            Path srcFilePath = srcUploadDir.resolve(filename);
            Files.write(srcFilePath, fileBytes, StandardOpenOption.CREATE);

            // Save to TARGET directory
            Path targetUploadDir = Paths.get("target/classes/static/uploads/users");
            if (!Files.exists(targetUploadDir)) {
                Files.createDirectories(targetUploadDir);
            }
            Path targetFilePath = targetUploadDir.resolve(filename);
            Files.write(targetFilePath, fileBytes, StandardOpenOption.CREATE);

            System.out.println("=== API AVATAR UPLOAD SUCCESS ===");
            System.out.println("Filename: " + filename);
            System.out.println("SRC: " + srcFilePath.toAbsolutePath());
            System.out.println("TARGET: " + targetFilePath.toAbsolutePath());

            // Return URL path
            return "/uploads/users/" + filename;

        } catch (Exception e) {
            System.err.println("=== API AVATAR UPLOAD FAILED: " + e.getMessage());
            throw new Exception("Failed to save avatar: " + e.getMessage());
        }
    }

    /**
     * Convert User entity to UserDTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setAvatar(user.getAvatar());
        dto.setEnabled(user.getEnabled());
        
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            dto.setRoles(user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toSet()));
        }
        
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        return dto;
    }
}
