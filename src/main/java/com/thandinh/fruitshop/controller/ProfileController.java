package com.thandinh.fruitshop.controller;

import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Controller
@RequestMapping("/thong-tin-ca-nhan")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Hiển thị trang profile
     */
    @GetMapping
    public String showProfile(Model model, HttpSession session) {
        // Get current logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/dang-nhap";
        }

        String email = auth.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        model.addAttribute("user", user);
        
        // Update session
        session.setAttribute("user", user);
        
        return "profile";
    }

    /**
     * Cập nhật thông tin cá nhân
     */
    @PostMapping("/cap-nhat")
    public String updateProfile(
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            // Update info
            user.setFullName(fullName != null && !fullName.isEmpty() ? fullName : user.getFullName());
            user.setPhone(phone);
            user.setAddress(address);

            userService.save(user);
            
            // Update session
            session.setAttribute("user", user);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
            return "redirect:/thong-tin-ca-nhan";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/thong-tin-ca-nhan";
        }
    }

    /**
     * Đổi mật khẩu
     */
    @PostMapping("/doi-mat-khau")
    public String changePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Validate
            if (newPassword.length() < 6) {
                redirectAttributes.addFlashAttribute("errorPassword", "Mật khẩu mới phải có ít nhất 6 ký tự!");
                return "redirect:/thong-tin-ca-nhan";
            }

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorPassword", "Mật khẩu mới không khớp!");
                return "redirect:/thong-tin-ca-nhan";
            }

            // Get current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            // Check current password
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                redirectAttributes.addFlashAttribute("errorPassword", "Mật khẩu hiện tại không đúng!");
                return "redirect:/thong-tin-ca-nhan";
            }

            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.save(user);

            redirectAttributes.addFlashAttribute("successPassword", "Đổi mật khẩu thành công!");
            return "redirect:/thong-tin-ca-nhan";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorPassword", "Lỗi: " + e.getMessage());
            return "redirect:/thong-tin-ca-nhan";
        }
    }

    /**
     * Cập nhật avatar
     */
    @PostMapping("/cap-nhat-avatar")
    public String updateAvatar(
            @RequestParam("avatar") MultipartFile avatar,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            if (avatar.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorAvatar", "Vui lòng chọn ảnh!");
                return "redirect:/thong-tin-ca-nhan";
            }

            // Get current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            // Save avatar
            String avatarUrl = saveUploadedFile(avatar);
            user.setAvatar(avatarUrl);
            userService.save(user);
            
            // Update session
            session.setAttribute("user", user);

            redirectAttributes.addFlashAttribute("successAvatar", "Cập nhật avatar thành công!");
            return "redirect:/thong-tin-ca-nhan";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorAvatar", "Lỗi: " + e.getMessage());
            return "redirect:/thong-tin-ca-nhan";
        }
    }

    /**
     * Save uploaded file to both src and target directories
     */
    private String saveUploadedFile(MultipartFile file) throws Exception {
        try {
            // Validate file
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

            // Save to SOURCE directory (src/main/resources/static/uploads/users/)
            Path srcUploadDir = Paths.get("src/main/resources/static/uploads/users");
            if (!Files.exists(srcUploadDir)) {
                Files.createDirectories(srcUploadDir);
            }
            Path srcFilePath = srcUploadDir.resolve(filename);
            Files.write(srcFilePath, fileBytes, StandardOpenOption.CREATE);

            // Save to TARGET directory (target/classes/static/uploads/users/)
            Path targetUploadDir = Paths.get("target/classes/static/uploads/users");
            if (!Files.exists(targetUploadDir)) {
                Files.createDirectories(targetUploadDir);
            }
            Path targetFilePath = targetUploadDir.resolve(filename);
            Files.write(targetFilePath, fileBytes, StandardOpenOption.CREATE);

            System.out.println("=== AVATAR UPLOAD SUCCESS ===");
            System.out.println("Filename: " + filename);
            System.out.println("SRC path: " + srcFilePath.toAbsolutePath());
            System.out.println("TARGET path: " + targetFilePath.toAbsolutePath());

            // Return URL path
            return "/uploads/users/" + filename;

        } catch (Exception e) {
            System.err.println("=== AVATAR UPLOAD FAILED: " + e.getMessage());
            throw new Exception("Failed to save avatar: " + e.getMessage());
        }
    }
}
