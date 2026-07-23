package com.jinsol.stockmate.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryRequest {

    @NotBlank(message = "카테고리명은 필수입니다.")
    private String name;

}
