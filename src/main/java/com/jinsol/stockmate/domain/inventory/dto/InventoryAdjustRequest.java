package com.jinsol.stockmate.domain.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class InventoryAdjustRequest {

    @NotNull(message = "수량은 필수입니다.")
    @Positive(message = "수량은 0보다 커야합니다.")
    private  Integer amount;
}
