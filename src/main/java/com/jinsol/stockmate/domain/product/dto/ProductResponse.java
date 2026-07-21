package com.jinsol.stockmate.domain.product.dto;

import com.jinsol.stockmate.domain.product.entity.Product;
import com.jinsol.stockmate.domain.product.enums.ProductStatus;
import lombok.Getter;

@Getter
public class ProductResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final int price;
    private final String currency;
    private final ProductStatus status;
    private final String categoryName;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.currency = product.getCurrency();
        this.status = product.getStatus();
        this.categoryName = product.getCategory().getName();
    }
}
