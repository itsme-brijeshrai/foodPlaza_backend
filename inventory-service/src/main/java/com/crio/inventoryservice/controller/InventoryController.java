package com.crio.inventoryservice.controller;

import com.crio.inventoryservice.dto.InventoryResponse;
import com.crio.inventoryservice.service.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    @Autowired
    private final IInventoryService inventoryService;

    @GetMapping("/isInStock")
    public ResponseEntity<List<InventoryResponse>> isInStock(@RequestParam List<String> skuCode) {
        return new ResponseEntity<>(inventoryService.isInStock(skuCode), HttpStatus.OK);
    }

    @PostMapping("/reduceQuantity")
    public ResponseEntity<String> reduceQuantity(@RequestParam String skuCode, @RequestParam int quantity) {
        try {
            inventoryService.reduceQuantity(skuCode, quantity);
            return new ResponseEntity<>("Inventory quantity reduced successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
