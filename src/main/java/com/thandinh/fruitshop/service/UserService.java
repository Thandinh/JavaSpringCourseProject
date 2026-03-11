package com.thandinh.fruitshop.service;

import com.thandinh.fruitshop.entity.Role;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.exception.ResourceNotFoundException;
import com.thandinh.fruitshop.repository.RoleRepository;
import com.thandinh.fruitshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAllActive(); // Only get non-deleted users
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAllActive(pageable); // Only get non-deleted users
    }

    public Optional<User> findById(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id); // Only get non-deleted user
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email); // Only get non-deleted user
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email).isPresent(); // Only check non-deleted
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User createUser(String email, String password, String fullName, String roleName, Boolean enabled) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName != null ? fullName : "");
        user.setEnabled(enabled != null ? enabled : true);

        // Set role
        Set<Role> roles = new HashSet<>();
        Optional<Role> roleOpt = roleRepository.findByName(roleName != null ? roleName : "ROLE_USER");
        if (roleOpt.isPresent()) {
            roles.add(roleOpt.get());
        } else {
            // If role doesn't exist, create it
            Role newRole = new Role();
            newRole.setName(roleName != null ? roleName : "ROLE_USER");
            roles.add(roleRepository.save(newRole));
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, String email, String password, String fullName, 
                          String phone, String address, String roleName, Boolean enabled) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("User", "id", id);
        }

        User user = userOpt.get();
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }
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
            }
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        // Hard delete - not recommended, use softDelete instead
        userRepository.deleteById(id);
    }

    @Transactional
    public void softDelete(Long id, String deletedBy) {
        System.out.println("=== UserService.softDelete: ID=" + id + ", deletedBy=" + deletedBy);
        
        Optional<User> userOpt = userRepository.findById(id); // Get even if deleted
        System.out.println("=== UserService.softDelete: User found=" + userOpt.isPresent());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("=== UserService.softDelete: User email=" + user.getEmail());
            System.out.println("=== UserService.softDelete: Before - deletedAt=" + user.getDeletedAt());
            
            user.softDelete(deletedBy);
            
            System.out.println("=== UserService.softDelete: After - deletedAt=" + user.getDeletedAt() + ", deletedBy=" + user.getDeletedBy());
            
            userRepository.save(user);
            System.out.println("=== UserService.softDelete: User saved successfully");
        } else {
            System.err.println("=== UserService.softDelete: User not found with id: " + id);
            throw new ResourceNotFoundException("User", "id", id);
        }
    }

    @Transactional
    public void restore(Long id) {
        Optional<User> userOpt = userRepository.findById(id); // Get even if deleted
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.restore();
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("User", "id", id);
        }
    }

    public long count() {
        return userRepository.countActive(); // Only count non-deleted users
    }
}
