package com.thandinh.fruitshop.controller.admin;

import com.thandinh.fruitshop.entity.Role;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.repository.RoleRepository;
import com.thandinh.fruitshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "uploads/users/";

    /**
     * Display list of all users with pagination
     */
    @GetMapping
    public String index(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "id") String sortBy,
                       @RequestParam(defaultValue = "desc") String sortDir,
                       Model model) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> userPage = userService.findAll(pageable);
            
            // Debug: Log avatar URLs
            System.out.println("=== DEBUGGING AVATAR URLS ===");
            userPage.getContent().forEach(user -> {
                if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                    System.out.println("USER ID=" + user.getId() 
                        + ", Email=" + user.getEmail() 
                        + ", Avatar URL=" + user.getAvatar());
                    
                    // Check if file exists
                    String filePath = "target/classes/static" + user.getAvatar();
                    File file = new File(filePath);
                    System.out.println("  TARGET FILE: " + filePath + " → Exists=" + file.exists());
                    
                    if (!file.exists()) {
                        // Try source path
                        String srcFilePath = "src/main/resources/static" + user.getAvatar();
                        File srcFile = new File(srcFilePath);
                        System.out.println("  SRC FILE: " + srcFilePath + " → Exists=" + srcFile.exists());
                    }
                }
            });
            System.out.println("=== END DEBUGGING ===");
            
            model.addAttribute("userPage", userPage);
            model.addAttribute("userList", userPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userPage.getTotalPages());
            model.addAttribute("totalItems", userPage.getTotalElements());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
        } catch (Exception e) {
            List<User> userList = userService.findAll();
            model.addAttribute("userList", userList);
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 1);
        }
        
        return "admin/user/index";
    }

    /**
     * Show create user form
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user/create";
    }

    /**
     * Handle user creation
     */
    @PostMapping("/create")
    public String create(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        @RequestParam(value = "fullName", required = false) String fullName,
                        @RequestParam(value = "phone", required = false) String phone,
                        @RequestParam(value = "address", required = false) String address,
                        @RequestParam(value = "roleName", defaultValue = "ROLE_USER") String roleName,
                        @RequestParam(value = "enabled", defaultValue = "true") Boolean enabled,
                        @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                        RedirectAttributes redirectAttributes) {
        
        try {
            // Check if email already exists
            if (userService.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Email already exists!");
                return "redirect:/admin/users/create";
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
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid role selected!");
                return "redirect:/admin/users/create";
            }
            user.setRoles(roles);

            // Handle file upload
            if (avatar != null && !avatar.isEmpty()) {
                System.out.println("=== AVATAR UPLOAD: File name: " + avatar.getOriginalFilename());
                System.out.println("=== AVATAR UPLOAD: File size: " + avatar.getSize());
                String avatarUrl = saveUploadedFile(avatar);
                System.out.println("=== AVATAR UPLOAD: Saved URL: " + avatarUrl);
                user.setAvatar(avatarUrl);
            } else {
                System.out.println("=== AVATAR UPLOAD: No avatar uploaded");
            }

            userService.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating user: " + e.getMessage());
            return "redirect:/admin/users/create";
        }

        return "redirect:/admin/users";
    }

    /**
     * Show edit user form
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.findById(id);
        
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
            return "redirect:/admin/users";
        }

        model.addAttribute("user", userOpt.get());
        return "admin/user/edit";
    }

    /**
     * Handle user update
     */
    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,
                        @RequestParam("email") String email,
                        @RequestParam(value = "password", required = false) String password,
                        @RequestParam(value = "fullName", required = false) String fullName,
                        @RequestParam(value = "phone", required = false) String phone,
                        @RequestParam(value = "address", required = false) String address,
                        @RequestParam(value = "roleName", required = false) String roleName,
                        @RequestParam(value = "enabled", defaultValue = "true") Boolean enabled,
                        @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                        RedirectAttributes redirectAttributes) {
        
        try {
            Optional<User> userOpt = userService.findById(id);
            
            if (userOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
                return "redirect:/admin/users";
            }

            User user = userOpt.get();
            
            // Update password only if provided
            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }
            
            if (fullName != null) {
                user.setFullName(fullName);
            }
            user.setPhone(phone);
            user.setAddress(address);
            user.setEnabled(enabled);

            // Update role if provided
            if (roleName != null && !roleName.isEmpty()) {
                Set<Role> roles = new HashSet<>();
                Optional<Role> roleOpt = roleRepository.findByName(roleName);
                if (roleOpt.isPresent()) {
                    roles.add(roleOpt.get());
                    user.setRoles(roles);
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

            userService.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    /**
     * Soft delete user (mark as deleted)
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== DELETE USER: Starting delete for ID: " + id);
            
            Optional<User> userOpt = userService.findById(id);
            System.out.println("=== DELETE USER: User found: " + userOpt.isPresent());
            
            if (userOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
                return "redirect:/admin/users";
            }

            // Get current user to prevent self-deletion
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = auth != null ? auth.getName() : "system";
            System.out.println("=== DELETE USER: Current user: " + currentUserEmail);
            System.out.println("=== DELETE USER: Target user: " + userOpt.get().getEmail());
            
            if (auth != null && auth.getName().equals(userOpt.get().getEmail())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You cannot delete your own account!");
                return "redirect:/admin/users";
            }

            // Soft delete (mark as deleted)
            System.out.println("=== DELETE USER: Calling softDelete...");
            userService.softDelete(id, currentUserEmail);
            System.out.println("=== DELETE USER: Soft delete completed successfully");
            
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
            
        } catch (Exception e) {
            System.err.println("=== DELETE USER ERROR: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    /**
     * Show current user profile page
     */
    @GetMapping("/profile")
    public String showProfile(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return "redirect:/login";
            }

            String email = auth.getName();
            Optional<User> userOpt = userService.findByEmail(email);
            
            if (userOpt.isEmpty()) {
                return "redirect:/login";
            }

            model.addAttribute("user", userOpt.get());
            return "admin/user/profile";
            
        } catch (Exception e) {
            return "redirect:/admin/dashboard";
        }
    }

    /**
     * Update current user profile
     */
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam(value = "fullName", required = false) String fullName,
                               @RequestParam(value = "phone", required = false) String phone,
                               @RequestParam(value = "address", required = false) String address,
                               @RequestParam(value = "currentPassword", required = false) String currentPassword,
                               @RequestParam(value = "newPassword", required = false) String newPassword,
                               @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                               RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return "redirect:/login";
            }

            String email = auth.getName();
            Optional<User> userOpt = userService.findByEmail(email);
            
            if (userOpt.isEmpty()) {
                return "redirect:/login";
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
                    redirectAttributes.addFlashAttribute("errorMessage", "Please provide current password!");
                    return "redirect:/admin/users/profile";
                }
                
                if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Current password is incorrect!");
                    return "redirect:/admin/users/profile";
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

            userService.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }

        return "redirect:/admin/users/profile";
    }

    /**
     * Save uploaded file
     */
    private String saveUploadedFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        System.out.println("=== saveUploadedFile: Starting...");

        // Generate unique filename first
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
            : "";
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        
        System.out.println("=== saveUploadedFile: Original filename: " + originalFilename);
        System.out.println("=== saveUploadedFile: Unique filename: " + uniqueFilename);

        // Save to BOTH src and target to work in both dev and runtime
        Path srcPath = Paths.get("src/main/resources/static/" + UPLOAD_DIR + uniqueFilename);
        Path targetPath = Paths.get("target/classes/static/" + UPLOAD_DIR + uniqueFilename);
        
        // Ensure parent directories exist
        Files.createDirectories(srcPath.getParent());
        Files.createDirectories(targetPath.getParent());
        
        System.out.println("=== saveUploadedFile: Saving to SRC: " + srcPath.toAbsolutePath());
        System.out.println("=== saveUploadedFile: Saving to TARGET: " + targetPath.toAbsolutePath());
        
        // Get file bytes once
        byte[] fileBytes = file.getBytes();
        
        // Save to both locations
        Files.write(srcPath, fileBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(targetPath, fileBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        
        System.out.println("=== saveUploadedFile: File saved successfully to both locations");
        System.out.println("=== saveUploadedFile: SRC exists: " + Files.exists(srcPath));
        System.out.println("=== saveUploadedFile: TARGET exists: " + Files.exists(targetPath));

        // Return URL path
        String url = "/" + UPLOAD_DIR + uniqueFilename;
        System.out.println("=== saveUploadedFile: Returning URL: " + url);
        
        return url;
    }

    /**
     * Delete file
     */
    private void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null && fileUrl.startsWith("/uploads/")) {
                String filePath = "src/main/resources/static" + fileUrl;
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
}
