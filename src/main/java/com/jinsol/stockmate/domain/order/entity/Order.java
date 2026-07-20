package com.jinsol.stockmate.domain.order.entity;

import com.jinsol.stockmate.domain.order.enums.OrderStatus;
import com.jinsol.stockmate.domain.user.entity.User;
import com.jinsol.stockmate.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //user_id(구매자)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //주문상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    //총 가격
    @Column(nullable = false)
    private int totalPrice;

    //통화
    @Column(nullable = false, length = 3)
    private String currency;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(User user, OrderStatus status, int totalPrice, String currency) {
        this.user = user;
        this.status = status;
        this.totalPrice = totalPrice;
        this.currency = currency;
    }

    public void changeStatus(OrderStatus status){
        this.status = status;
    }

    //연관관계 편의 메서드
    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }
}
