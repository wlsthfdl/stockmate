package com.jinsol.stockmate.domain.inventory.dto;

import com.jinsol.stockmate.domain.inventory.entity.Inventory;
import lombok.Getter;

@Getter
public class InventoryResponse {

    private final Long id;
    private final Long productId;
    private final String productName;
    private final int quantity;

    public InventoryResponse(Inventory inventory){
        this.id = inventory.getId();
        this.productId = inventory.getProduct().getId();
        this.productName = inventory.getProduct().getName();
        this.quantity = inventory.getQuantity();
    }

}
