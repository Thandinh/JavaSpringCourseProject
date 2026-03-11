package com.thandinh.fruitshop.service;

import com.thandinh.fruitshop.dto.LoginDTO;
import com.thandinh.fruitshop.dto.RegisterDTO;
import com.thandinh.fruitshop.entity.Role;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.exception.BadRequestException;
import com.thandinh.fruitshop.exception.DuplicateResourceException;
import com.thandinh.fruitshop.exception.ForbiddenException;
import com.thandinh.fruitshop.exception.UnauthorizedException;
import com.thandinh.fruitshop.repository.RoleRepository;
import com.thandinh.fruitshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Gmail regex pattern
    private static final Pattern GMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@gmail\\.com$",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Đăng ký tài khoản mới
     */
    @Transactional
    public User register(RegisterDTO registerDTO) {
        // 1. Kiểm tra email đã tồn tại chưa (chỉ check user chưa bị xóa)
        if (userRepository.findByEmailAndDeletedAtIsNull(registerDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email này đã được đăng ký!");
        }

        // 2. Kiểm tra email phải là Gmail
        if (!isGmail(registerDTO.getEmail())) {
            throw new BadRequestException("Vui lòng sử dụng địa chỉ Gmail để đăng ký!");
        }

        // 3. Kiểm tra mật khẩu khớp nhau
        if (!registerDTO.getPassword().equals(registerDTO.getPasswordConfirmation())) {
            throw new BadRequestException("Mật khẩu nhập lại không khớp!");
        }

        // 4. Kiểm tra độ dài mật khẩu
        if (registerDTO.getPassword().length() < 6) {
            throw new BadRequestException("Mật khẩu phải có ít nhất 6 ký tự!");
        }

        // 5. Tạo user mới
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setFullName(registerDTO.getFullName());
        user.setPhone(registerDTO.getPhone());
        user.setAddress(registerDTO.getAddress());
        user.setEnabled(true);

        // 6. Gán role USER mặc định
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    // Nếu chưa có role USER, tạo mới
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // 7. Lưu user
        return userRepository.save(user);
    }

    /**
     * Đăng nhập (dùng cho API)
     * Note: Web form login sử dụng Spring Security
     */
    public User login(LoginDTO loginDTO) {
        // 1. Tìm user theo email (chỉ user chưa bị xóa)
        Optional<User> userOptional = userRepository.findByEmailAndDeletedAtIsNull(loginDTO.getEmail());
        
        if (userOptional.isEmpty()) {
            throw new UnauthorizedException("Email hoặc mật khẩu không đúng!");
        }

        User user = userOptional.get();

        // 2. Kiểm tra tài khoản có bị khóa không
        if (!user.getEnabled()) {
            throw new ForbiddenException("Tài khoản của bạn đã bị khóa!");
        }

        // 3. Kiểm tra mật khẩu
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Email hoặc mật khẩu không đúng!");
        }

        return user;
    }

    /**
     * Kiểm tra email có phải Gmail không
     */
    public boolean isGmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return GMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
