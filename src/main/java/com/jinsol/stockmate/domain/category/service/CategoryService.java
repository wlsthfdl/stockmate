package com.jinsol.stockmate.domain.category.service;

import com.jinsol.stockmate.domain.category.dto.CategoryRequest;
import com.jinsol.stockmate.domain.category.dto.CategoryResponse;
import com.jinsol.stockmate.domain.category.entity.Category;
import com.jinsol.stockmate.domain.category.repository.CategoryRepository;
import com.jinsol.stockmate.domain.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //카테고리 등록
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request){
        Category category = Category.builder()
                .name(request.getName())
                .build();
        Category saveCategory = categoryRepository.save(category);
        return new CategoryResponse(saveCategory);
    }

    //카테고리 목록 조회
    public List<CategoryResponse> getCategories(){
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::new)
                .toList();
    }

    //카테고리 수정
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        category.changeName(request.getName());

        return new CategoryResponse(category);
    }

    //카테고리 삭제
    @Transactional
    public void deleteCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        categoryRepository.delete(category);
    }

}
