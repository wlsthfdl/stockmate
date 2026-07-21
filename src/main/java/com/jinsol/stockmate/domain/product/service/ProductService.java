package com.jinsol.stockmate.domain.product.service;

import com.jinsol.stockmate.domain.category.entity.Category;
import com.jinsol.stockmate.domain.category.repository.CategoryRepository;
import com.jinsol.stockmate.domain.product.dto.ProductCreateRequest;
import com.jinsol.stockmate.domain.product.dto.ProductResponse;
import com.jinsol.stockmate.domain.product.dto.ProductUpdateRequest;
import com.jinsol.stockmate.domain.product.entity.Product;
import com.jinsol.stockmate.domain.product.enums.ProductStatus;
import com.jinsol.stockmate.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    //상품 등록
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .status(ProductStatus.ON_SALE)
                .category(category)
                .build();

        Product saveProduct = productRepository.save(product);
        return new ProductResponse(saveProduct);
    }

    //상품 단건 조회
    public ProductResponse getProduct(Long productId){
        Product product = productRepository.findById(productId)
        .orElseThrow(()-> new IllegalArgumentException(""));

        return new ProductResponse(product);
    }

    //상품 목록 조회
    public java.util.List<ProductResponse> getProducts(){
        return productRepository.findAll().stream().
                map(ProductResponse::new).toList();
    }

    //상품 수정
    @Transactional
    public ProductResponse updateProduct(Long productId,  ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        if (request.getName() != null) {
            product.changeName(request.getName());
        }
        if (request.getDescription() != null) {
            product.changeDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.changePrice(request.getPrice());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
            product.changeCategory(category);
        }
    return new ProductResponse(product);

    }

}
