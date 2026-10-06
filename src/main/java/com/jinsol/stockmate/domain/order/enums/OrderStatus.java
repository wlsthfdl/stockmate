package com.jinsol.stockmate.domain.order.enums;

public enum OrderStatus {
    /* 추후 결제 API 연동 시 PENDING 상태 추가 예정 */
    PAID,        // 결제 완료
    PREPARING,   // 상품 준비중
    SHIPPED,     // 배송중
    DELIVERED,   // 배송완료
    CANCELED;    // 취소

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case PAID      -> next == PREPARING;
            case PREPARING -> next == SHIPPED;
            case SHIPPED   -> next == DELIVERED;
            case DELIVERED, CANCELED -> false;   // 끝난 주문은 더 못 바꿈
        };
    }

    public boolean canCancel() {
        return this == PAID || this == PREPARING;
    }
}
