package com.crio.inventoryservice.repository;

import com.crio.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IInventoryRepository extends JpaRepository<Inventory,Long> {
    List<Inventory> findByskuCodeIn(List<String> skuCode);

}
