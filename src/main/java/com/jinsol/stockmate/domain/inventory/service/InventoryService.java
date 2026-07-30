package com.jinsol.stockmate.domain.inventory.service;

import com.jinsol.stockmate.domain.inventory.dto.InventoryAdjustRequest;
import com.jinsol.stockmate.domain.inventory.dto.InventoryResponse;
import com.jinsol.stockmate.domain.inventory.entity.Inventory;
import com.jinsol.stockmate.domain.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    //재고 단건 조회
    public InventoryResponse getInventoryByProductId(Long productId){
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 재고입니다."));
        return new InventoryResponse(inventory);
    }

    //재고 목록 조회
    public List<InventoryResponse> getInventories(){
        return inventoryRepository.findAll().stream()
                .map(InventoryResponse::new)
                .toList();
    }

    //입고처리
    @Transactional
    public InventoryResponse increaseStock(Long inventoryId, InventoryAdjustRequest request){
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 재고입니다."));

        inventory.increase(request.getAmount());

        return  new InventoryResponse(inventory);
    }

    // 출고 처리
    @Transactional
    public InventoryResponse decreaseStock(Long inventoryId, InventoryAdjustRequest request) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 재고입니다."));

        inventory.decrease(request.getAmount());

        return new InventoryResponse(inventory);
    }
}
