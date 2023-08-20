package com.crio.orderservice.service;

import com.crio.orderservice.dto.InventoryResponse;
import com.crio.orderservice.dto.OrderLineItemsDto;
import com.crio.orderservice.dto.OrderRequest;
import com.crio.orderservice.model.Order;
import com.crio.orderservice.model.OrderLineItems;
import com.crio.orderservice.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    @Autowired
    private final IOrderRepository iOrderRepository;
    @Autowired
    private final WebClient webClient;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtosList().stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Call inventory service and get inventory responses
        InventoryResponse[] inventoryResponsesArray = webClient.get()
                .uri("http://localhost:8082/api/inventory/isInStock",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        if (inventoryResponsesArray != null) {
            Map<String, InventoryResponse> inventoryResponseMap = Arrays.stream(inventoryResponsesArray)
                    .collect(Collectors.toMap(InventoryResponse::getSkuCode, Function.identity()));

            for (OrderLineItems lineItem : order.getOrderLineItemsList()) {
                InventoryResponse inventoryResponse = inventoryResponseMap.get(lineItem.getSkuCode());

                if (inventoryResponse == null) {
                    log.info("{} not available in inventory",lineItem.getSkuCode());
                    throw new IllegalArgumentException(lineItem.getSkuCode()+" not available ");
                }

                int requestedQuantity = lineItem.getQuantity();
                int availableQuantity = inventoryResponse.getQuantity();

                if (requestedQuantity > availableQuantity) {
                    log.info("Only {} stocks are available for {}",availableQuantity,inventoryResponse.getSkuCode());
                    throw new IllegalArgumentException("Only "+inventoryResponse.getQuantity()+" piece available for "+inventoryResponse.getSkuCode());
                }
                iOrderRepository.save(order);
                log.info("Order Placed Successfully with OrderId: {}", order.getId());
                webClient.post()
                        .uri("http://localhost:8082/api/inventory/reduceQuantity?skuCode={skuCode}&quantity={quantity}",
                                lineItem.getSkuCode(), lineItem.getQuantity())
                        .retrieve()
                        .toBodilessEntity()
                        .block();
            }
        }
    }



    @Override
    public void deleteOrderRequest(Long id) {
        Optional<Order> opt = iOrderRepository.findById(id);
        if (opt.isPresent()) {
            Order order = opt.get();
            iOrderRepository.delete(order);
            log.info("OrderId {} rejected", id);
        } else {
            log.warn("No such order found with given orderId");
        }
    }

    @Override
    public List<Order> getAllOrder() {
        List<Order> orders = iOrderRepository.findAll();
        if (!(orders.size() > 0)) {
            log.info("Currently we don't have any orders");
        } else {
            log.info("{}", orders);
        }
        return orders;
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
