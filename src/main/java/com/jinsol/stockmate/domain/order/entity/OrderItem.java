package com.jinsol.stockmate.domain.order.entity;

import com.jinsol.stockmate.domain.product.entity.Product;
import com.jinsol.stockmate.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    //수량
    @Column(nullable = false)
    private int quantity;

    // 주문 당시 상품 가격 (스냅샷)
    @Column(nullable = false)
    private int price;

    @Builder
    public OrderItem(Order order, Product product, int quantity, int price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
