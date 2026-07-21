package com.jinsol.stockmate.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ProductUpdateRequest {

    private String name;

    private String description;

    @Positive(message = "가격은 0보다 커야 합니다.")
    private Integer price;

    private String currency;

    private Long categoryId;
}