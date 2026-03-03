package com.thandinh.fruitshop.repository;

import com.thandinh.fruitshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Find category by name
     */
    Optional<Category> findByName(String name);
    
    /**
     * Check if category exists by name
     */
    boolean existsByName(String name);
    
    /**
     * Check if category exists by name and id not equal
     */
    boolean existsByNameAndIdNot(String name, Long id);
}
