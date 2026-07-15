package com.jinsol.stockmate.domain.product.entity;

import com.jinsol.stockmate.domain.product.enums.ProductStatus;
import com.jinsol.stockmate.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Builder
    public Product(String name, String description, int price, String currency, ProductStatus status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.status = status;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changePriceWithCurrency(int price, String currency) {
        this.price = price;
        this.currency = currency;
    }

    public void changeStatus(ProductStatus status) {
        this.status = status;
    }
}