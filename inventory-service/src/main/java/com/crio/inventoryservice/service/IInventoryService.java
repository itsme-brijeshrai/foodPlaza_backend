package com.crio.inventoryservice.service;

import com.crio.inventoryservice.dto.InventoryResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IInventoryService {
    List<InventoryResponse> isInStock(List<String> skuCode);

    void reduceQuantity(String skuCode, int quantity);
}
