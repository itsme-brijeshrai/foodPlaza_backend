package com.crio.orderservice.controller;

import com.crio.orderservice.dto.OrderRequest;
import com.crio.orderservice.model.Order;
import com.crio.orderservice.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final IOrderService iOrderService;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            iOrderService.placeOrder(orderRequest);
            return new ResponseEntity<>("Order placed successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllOrder")
    public ResponseEntity<List<Order>> getAllOrder() {
        return new ResponseEntity<>(iOrderService.getAllOrder(),HttpStatus.OK);
    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<String> deleteOrder (@PathVariable Long id) {
        iOrderService.deleteOrderRequest(id);
        return new ResponseEntity<>("OrderId "+id+" rejected ",HttpStatus.ACCEPTED);
    }
}
