package com.thandinh.fruitshop.repository;

import com.thandinh.fruitshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
