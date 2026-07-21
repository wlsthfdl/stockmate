package com.jinsol.stockmate.domain.product.repository;

import com.jinsol.stockmate.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //카테고리별 상품 조회
    java.util.List<Product> findByCategoryId(Long categoryId);
}
