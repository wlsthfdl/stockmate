package com.jinsol.stockmate.domain.product.controller;

import static com.jinsol.stockmate.global.security.SecurityUtils.isAdmin;
import org.springframework.security.access.prepost.PreAuthorize;
import com.jinsol.stockmate.domain.product.dto.ProductCreateRequest;
import com.jinsol.stockmate.domain.product.dto.ProductResponse;
import com.jinsol.stockmate.domain.product.dto.ProductUpdateRequest;
import com.jinsol.stockmate.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //상품 등록
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request){
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);     //201 + body
    }

    //상품 단건 조회
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Long productId,
            Authentication authentication){
        return ResponseEntity.ok(productService.getProduct(productId, isAdmin(authentication)));
    }

    //상품 목록 조회
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(Authentication authentication){
        return ResponseEntity.ok(productService.getProducts(isAdmin(authentication)));

    }

    //상품 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @Valid
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest request){
        ProductResponse response = productService.updateProduct(productId, request);
        return ResponseEntity.ok(response);
    }

    //상품 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();       //204 + body없음
    }

    //상품 비공개
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productId}/hide")
    public ResponseEntity<ProductResponse> hideProduct(@PathVariable Long productId){
        return ResponseEntity.ok(productService.hideProduct(productId));
    }

    //상품 공개
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productId}/show")
    public ResponseEntity<ProductResponse> showProduct(@PathVariable Long productId){
        return ResponseEntity.ok(productService.showProduct(productId));
    }

}
