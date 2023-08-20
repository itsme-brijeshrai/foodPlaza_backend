package com.crio.orderservice.service;

import com.crio.orderservice.dto.OrderRequest;
import com.crio.orderservice.model.Order;

import java.util.List;

public interface IOrderService {

    void placeOrder(OrderRequest orderRequest);

    void deleteOrderRequest(Long id);

    List<Order> getAllOrder();
}
