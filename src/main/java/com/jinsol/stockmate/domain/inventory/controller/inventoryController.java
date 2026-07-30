package com.jinsol.stockmate.domain.inventory.controller;

import com.jinsol.stockmate.domain.inventory.dto.InventoryAdjustRequest;
import com.jinsol.stockmate.domain.inventory.dto.InventoryResponse;
import com.jinsol.stockmate.domain.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventories")
@RequiredArgsConstructor
public class inventoryController {

    private final InventoryService inventoryService;

    //재고 목록 조회
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getInventories(){
        return ResponseEntity.ok(inventoryService.getInventories());
    }

    // 상품 기준 재고 단건 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    //입고처리
    @PostMapping("{inventoryId}/increase")
    public  ResponseEntity<InventoryResponse> increaseStock(
           @PathVariable Long inventoryId,
           @Valid @RequestBody InventoryAdjustRequest request){
        return ResponseEntity.ok(inventoryService.increaseStock(inventoryId, request));
    }

    // 출고 처리
    @PostMapping("/{inventoryId}/decrease")
    public ResponseEntity<InventoryResponse> decreaseStock(
            @PathVariable Long inventoryId,
            @Valid @RequestBody InventoryAdjustRequest request) {
        return ResponseEntity.ok(inventoryService.decreaseStock(inventoryId, request));
    }
}

