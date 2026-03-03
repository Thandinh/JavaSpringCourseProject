package com.thandinh.fruitshop.config;

import com.thandinh.fruitshop.entity.Role;
import com.thandinh.fruitshop.entity.User;
import com.thandinh.fruitshop.repository.RoleRepository;
import com.thandinh.fruitshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Migrate old roles (USER -> ROLE_USER, ADMIN -> ROLE_ADMIN)
        migrateOldRoles();
        
        // Tạo roles nếu chưa tồn tại
        createRoleIfNotExists("ROLE_USER");
        createRoleIfNotExists("ROLE_ADMIN");

        // Tạo admin user nếu chưa tồn tại
        createAdminUserIfNotExists();
    }

    private void migrateOldRoles() {
        // Migrate OLD "USER" to "ROLE_USER"
        roleRepository.findByName("USER").ifPresent(oldRole -> {
            oldRole.setName("ROLE_USER");
            roleRepository.save(oldRole);
            System.out.println("✅ Migrated role: USER -> ROLE_USER");
        });
        
        // Migrate OLD "ADMIN" to "ROLE_ADMIN"
        roleRepository.findByName("ADMIN").ifPresent(oldRole -> {
            oldRole.setName("ROLE_ADMIN");
            roleRepository.save(oldRole);
            System.out.println("✅ Migrated role: ADMIN -> ROLE_ADMIN");
        });
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            System.out.println("✅ Created role: " + roleName);
        }
    }

    private void createAdminUserIfNotExists() {
        String adminEmail = "admin@gmail.com";
        
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            // Tạo admin user
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrator");
            admin.setPhone("0000000000");
            admin.setAddress("Admin Address");
            admin.setEnabled(true);

            // Gán role ADMIN
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN role not found"));
            
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
            System.out.println("✅ Created admin user: " + adminEmail + " / admin123");
        } else {
            // Cập nhật admin user hiện có để đảm bảo có ADMIN role
            User admin = userRepository.findByEmail(adminEmail).get();
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN role not found"));
            
            if (!admin.getRoles().contains(adminRole)) {
                admin.getRoles().add(adminRole);
                userRepository.save(admin);
                System.out.println("✅ Updated admin user with ROLE_ADMIN role");
            } else {
                System.out.println("ℹ️  Admin user already exists with correct role: " + adminEmail);
            }
        }
    }
}
