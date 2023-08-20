package com.crio.inventoryservice.service;

import com.crio.inventoryservice.dto.InventoryResponse;
import com.crio.inventoryservice.model.Inventory;
import com.crio.inventoryservice.repository.IInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {

    @Autowired
    private final IInventoryRepository inventoryRepository;
    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findByskuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .quantity(inventory.getQuantity())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
    }

    @Override
    public void reduceQuantity(String skuCode, int quantity) {
        List<Inventory> inventories = inventoryRepository.findByskuCodeIn(List.of(skuCode));
        if (!inventories.isEmpty()) {
            Inventory inventory = inventories.get(0);
            int updatedQuantity = inventory.getQuantity() - quantity;
            if (updatedQuantity < 0) {
                throw new IllegalArgumentException("Insufficient stock for " + skuCode);
            }
            inventory.setQuantity(updatedQuantity);
            inventoryRepository.save(inventory);
        }
    }
}
