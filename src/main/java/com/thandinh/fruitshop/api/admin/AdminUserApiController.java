package com.thandinh.fruitshop.api.admin;

import com.thandinh.fruitshop.dto.UserDTO;
import com.thandinh.fruitshop.entity.Role;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.repository.RoleRepository;
import com.thandinh.fruitshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST API Controller for Admin User Management
 * Provides JSON endpoints for CRUD operations on users
 */
@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class AdminUserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "uploads/users/";

    /**
     * Get all users
     * GET /api/admin/users
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            List<User> users = userService.findAll();
            List<UserDTO> userDTOs = users.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Users retrieved successfully");
            response.put("data", userDTOs);
            response.put("total", userDTOs.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error retrieving users", e);
        }
    }

    /**
     * Get user by ID
     * GET /api/admin/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        try {
            Optional<User> userOpt = userService.findById(id);
            
            if (userOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User retrieved successfully");
            response.put("data", convertToDTO(userOpt.get()));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleError("Error retrieving user", e);
        }
    }

    /**
     * Create new user
     * POST /api/admin/users
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "roleName", defaultValue = "ROLE_USER") String roleName,
            @RequestParam(value = "enabled", defaultValue = "true") Boolean enabled,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        
        try {
            // Validate required fields
            if (email == null || email.trim().isEmpty()) {
                return createErrorResponse("Email is required", HttpStatus.BAD_REQUEST);
            }
            if (password == null || password.trim().isEmpty()) {
                return createErrorResponse("Password is required", HttpStatus.BAD_REQUEST);
            }

            // Check if email already exists
            if (userService.existsByEmail(email)) {
                return createErrorResponse("Email already exists", HttpStatus.CONFLICT);
            }

            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setFullName(fullName != null && !fullName.isEmpty() ? fullName : "");
            user.setPhone(phone);
            user.setAddress(address);
            user.setEnabled(enabled);

            // Set role
            Set<Role> roles = new HashSet<>();
            Optional<Role> roleOpt = roleRepository.findByName(roleName);
            if (roleOpt.isPresent()) {
                roles.add(roleOpt.get());
            } else {
                return createErrorResponse("Invalid role: " + roleName, HttpStatus.BAD_REQUEST);
            }
            user.setRoles(roles);

            // Handle file upload
            if (avatar != null && !avatar.isEmpty()) {
                String avatarUrl = saveUploadedFile(avatar);
                user.setAvatar(avatarUrl);
            }

            User savedUser = userService.save(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User created successfully");
            response.put("data", convertToDTO(savedUser));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            return handleError("Error creating user", e);
        }
    }

    /**
     * Update user
     * PUT /api/admin/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "roleName", required = false) String roleName,
            @RequestParam(value = "enabled", required = false) Boolean enabled,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        
        try {
            Optional<User> userOpt = userService.findById(id);
            
            if (userOpt.isEmpty()) {
                return createErrorResponse("User not found with ID: " + id, HttpStatus.NOT_FOUND);
            }

            User user = userOpt.get();
            
            // Update password only if provided
            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }
            
            if (fullName != null) {
                user.setFullName(fullName);
            }
            if (phone != null) {
                user.setPhone(phone);
            }
            if (address != null) {
                user.setAddress(address);
            }
            if (enabled != null) {
                user.setEnabled(enabled);
            }

            // Update role if provided
            if (roleName != null && !roleName.isEmpty()) {
                Set<Role> roles = new HashSet<>();
                Optional<Role> roleOpt = roleRepository.findByName(roleName);
                if (roleOpt.isPresent()) {
                    roles.add(roleOpt.get());
                    user.setRoles(roles);
                } else {
                    return createErrorResponse("Invalid role: " + roleName, HttpStatus.BAD_REQUEST);
                }
            }

            // Handle file upload
            if (avatar != null && !avatar.isEmpty()) {
                // Delete old avatar if exists
                if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                    deleteFile(user.getAvatar());
                }
                
                String avatarUrl = saveUploadedFile(avatar);
                user.setAvatar(avatarUrl);
            }

            User updatedUser = userService.save(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User updated successfully");
            response.put("data", convertToDTO(updatedUser));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return handleError("Error updating user", e);
        }
    }

    /**
     * Soft delete user (mark as deleted)
     * DELETE /api/admin/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        try {
            Optional<User> userOpt = userService.findById(id);
            
            if (userOpt.isEmpty()) {
                return createErrorResponse("User not found with ID: " + id, HttpStatus.NOT_FOUND);
            }

            // Get current user to prevent self-deletion
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName().equals(userOpt.get().getEmail())) {
                return createErrorResponse("You cannot delete your own account", HttpStatus.FORBIDDEN);
            }

            // Soft delete (mark as deleted)
            String currentUserEmail = auth != null ? auth.getName() : "system";
            userService.softDelete(id, currentUserEmail);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User deleted successfully");
            response.put("id", id);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return handleError("Error deleting user", e);
        }
    }

    /**
     * Restore deleted user
     * POST /api/admin/users/{id}/restore
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreUser(@PathVariable Long id) {
        try {
            userService.restore(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User restored successfully");
            response.put("id", id);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return handleError("Error restoring user", e);
        }
    }

    /**
     * Get current user profile
     * GET /api/admin/users/profile/current
     */
    @GetMapping("/profile/current")
    public ResponseEntity<Map<String, Object>> getCurrentUserProfile() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return createErrorResponse("User not authenticated", HttpStatus.UNAUTHORIZED);
            }

            String email = auth.getName();
            Optional<User> userOpt = userService.findByEmail(email);
            
            if (userOpt.isEmpty()) {
                return createErrorResponse("User not found", HttpStatus.NOT_FOUND);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile retrieved successfully");
            response.put("data", convertToDTO(userOpt.get()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return handleError("Error retrieving profile", e);
        }
    }

    /**
     * Update current user profile
     * PUT /api/admin/users/profile/current
     */
    @PutMapping("/profile/current")
    public ResponseEntity<Map<String, Object>> updateCurrentUserProfile(
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "currentPassword", required = false) String currentPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return createErrorResponse("User not authenticated", HttpStatus.UNAUTHORIZED);
            }

            String email = auth.getName();
            Optional<User> userOpt = userService.findByEmail(email);
            
            if (userOpt.isEmpty()) {
                return createErrorResponse("User not found", HttpStatus.NOT_FOUND);
            }

            User user = userOpt.get();
            
            if (fullName != null && !fullName.isEmpty()) {
                user.setFullName(fullName);
            }
            if (phone != null) {
                user.setPhone(phone);
            }
            if (address != null) {
                user.setAddress(address);
            }

            // Update password if provided and current password matches
            if (newPassword != null && !newPassword.isEmpty()) {
                if (currentPassword == null || currentPassword.isEmpty()) {
                    return createErrorResponse("Current password is required", HttpStatus.BAD_REQUEST);
                }
                
                if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                    return createErrorResponse("Current password is incorrect", HttpStatus.BAD_REQUEST);
                }
                
                user.setPassword(passwordEncoder.encode(newPassword));
            }

            // Handle avatar upload
            if (avatar != null && !avatar.isEmpty()) {
                // Delete old avatar if exists
                if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                    deleteFile(user.getAvatar());
                }
                
                String avatarUrl = saveUploadedFile(avatar);
                user.setAvatar(avatarUrl);
            }

            User updatedUser = userService.save(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile updated successfully");
            response.put("data", convertToDTO(updatedUser));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return handleError("Error updating profile", e);
        }
    }

    /**
     * Get user count statistics
     * GET /api/admin/users/statistics/count
     */
    @GetMapping("/statistics/count")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        try {
            long totalUsers = userService.count();
            List<User> allUsers = userService.findAll();
            
            long activeUsers = allUsers.stream().filter(User::getEnabled).count();
            long inactiveUsers = totalUsers - activeUsers;
            
            Map<String, Long> statistics = new HashMap<>();
            statistics.put("total", totalUsers);
            statistics.put("active", activeUsers);
            statistics.put("inactive", inactiveUsers);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Statistics retrieved successfully");
            response.put("data", statistics);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return handleError("Error retrieving statistics", e);
        }
    }

    /**
     * Convert User entity to UserDTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setAvatar(user.getAvatar());
        dto.setEnabled(user.getEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        // Convert roles to set of strings
        if (user.getRoles() != null) {
            Set<String> roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        }
        
        return dto;
    }

    /**
     * Save uploaded file
     */
    private String saveUploadedFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // Create directory if not exists
        String uploadPath = "src/main/resources/static/" + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
            : "";
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Save file
        Path filePath = Paths.get(uploadPath + uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/assets/images/" + UPLOAD_DIR + uniqueFilename;
    }

    /**
     * Delete file
     */
    private void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null && fileUrl.startsWith("/assets/images/")) {
                String filePath = "src/main/resources/static" + fileUrl.replace("/assets/images/", "/");
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            // Log error but don't throw exception
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }

    /**
     * Create error response
     */
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Handle exceptions and create error response
     */
    private ResponseEntity<Map<String, Object>> handleError(String message, Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("error", e.getMessage());
        
        e.printStackTrace(); // Log the error
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
