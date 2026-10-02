package com.jinsol.stockmate.domain.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {
    @NotEmpty(message = "주문 상품은 1개 이상이어야 합니다.")
    @Valid
    private List<OrderItemRequest> items;

}
