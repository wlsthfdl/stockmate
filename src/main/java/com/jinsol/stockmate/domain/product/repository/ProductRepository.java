package com.jinsol.stockmate.domain.product.repository;

import com.jinsol.stockmate.domain.product.entity.Product;
import com.jinsol.stockmate.domain.product.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //카테고리별 상품 조회
    java.util.List<Product> findByCategoryId(Long categoryId);

    //특정 상태를 제외하고 조회(삭제된 상품 제외)
    List<Product> findByStatusNot(ProductStatus status);
}
