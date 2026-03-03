package com.thandinh.fruitshop.repository;

import com.thandinh.fruitshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    // Soft delete queries - only get non-deleted users
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    
    Optional<User> findByIdAndDeletedAtIsNull(Long id);
    
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL")
    List<User> findAllActive();
    
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL")
    Page<User> findAllActive(Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL")
    long countActive();
}
