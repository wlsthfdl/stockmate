package com.jinsol.stockmate.domain.order.entity;

import com.jinsol.stockmate.domain.order.enums.OrderStatus;
import com.jinsol.stockmate.domain.user.entity.User;
import com.jinsol.stockmate.global.common.BaseEntity;
import com.jinsol.stockmate.global.exception.InvalidOrderStatusException;
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

    public void changeStatus(OrderStatus next){
        if(!this.status.canTransitionTo(next)){
            throw new InvalidOrderStatusException(
                    "주문 상태를 " + this.status + "에서 " + next + "(으)로 변경할 수 없습니다.");
        }
        this.status = next;
    }

    //연관관계 편의 메서드
    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    public void changeTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void cancel() {
        if (!this.status.canCancel()) {
            throw new InvalidOrderStatusException(
                    this.status + " 상태의 주문은 취소할 수 없습니다.");
        }
        this.status = OrderStatus.CANCELED;
    }
}
