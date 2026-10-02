package com.jinsol.stockmate.domain.order.dto;

import com.jinsol.stockmate.domain.order.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {

    private final Long productId;
    private final String productName;
    private final int quantity;
    private final int price;

    public OrderItemResponse(OrderItem orderItem) {
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProduct().getName();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }
}
