package com.jinsol.stockmate.domain.product.enums;

import java.util.List;

public enum ProductStatus {
    ON_SALE,    // 판매중
    SOLD_OUT,   // 품절
    HIDDEN,     // 비공개(판매 중지)
    DELETED;    // 삭제(영구, 목록에서 제외)

    // USER에게 노출되는 상태 목록(화이트 리스트)
    public  static List<ProductStatus> customerVisible() {
        return List.of(ON_SALE, SOLD_OUT);
    }

    public boolean isVisibleToCustomer() {
        return customerVisible().contains(this);
    }

    // 주문 가능한 상태인지 (판매중만 가능)
    public boolean isOrderable() {
        return this == ON_SALE;
    }
}