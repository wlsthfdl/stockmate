package com.jinsol.stockmate.domain.inventory.entity;

import com.jinsol.stockmate.domain.product.entity.Product;
import com.jinsol.stockmate.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
Table inventories {
  id bigint [pk]
  product_id bigint [ref: > products.id, unique]
  quantity int
  created_at timestamp
  updated_at timestamp
}
* */
@Entity
@Table(name = "inventories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="product_id", nullable = false, unique = true)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Builder
    public Inventory(Product product, int quantity){
        this.product = product;
        this.quantity =quantity;
    }

    public void increase(int amount){
        this.quantity += amount;
    }

    public void decrease (int amount){
        if(this.quantity < amount){
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.quantity -= amount;
    }
}
