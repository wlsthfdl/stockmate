package com.jinsol.stockmate.domain.category.repository;

import com.jinsol.stockmate.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}