package com.jinsol.stockmate.domain.product.controller;

import com.jinsol.stockmate.domain.product.dto.ProductCreateRequest;
import com.jinsol.stockmate.domain.product.dto.ProductResponse;
import com.jinsol.stockmate.domain.product.dto.ProductUpdateRequest;
import com.jinsol.stockmate.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //상품 등록
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request){
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);     //201 + body
    }

    //상품 단건 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId){
        ProductResponse response = productService.getProduct(productId);
        return ResponseEntity.ok(response);     //200 + body
    }

    //상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(){
        List<ProductResponse> responses= productService.getProducts();
        return ResponseEntity.ok(responses);
    }

    //상품 수정
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest request){
        ProductResponse response = productService.updateProduct(productId, request);
        return ResponseEntity.ok(response);
    }

    //상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();       //204 + body없음
    }
}
