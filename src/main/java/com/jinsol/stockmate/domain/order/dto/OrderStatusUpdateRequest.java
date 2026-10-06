package com.jinsol.stockmate.domain.order.dto;

import com.jinsol.stockmate.domain.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    @NotNull(message = "변경할 주문 상태는 필수입니다.")
    private OrderStatus status;
}
