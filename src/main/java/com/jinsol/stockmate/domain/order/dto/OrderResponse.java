package com.jinsol.stockmate.domain.order.dto;

import com.jinsol.stockmate.domain.order.entity.Order;
import com.jinsol.stockmate.domain.order.enums.OrderStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderResponse {

    private final Long id;
    private final OrderStatus status;
    private final int totalPrice;
    private final String currency;
    private final List<OrderItemResponse> items;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.currency = order.getCurrency();
        this.items = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .toList();
    }
}