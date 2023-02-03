package com.Silvador386.ordering.service;

import com.Silvador386.ordering.dto.InventoryResponse;
import com.Silvador386.ordering.dto.OrderLineItemsDto;
import com.Silvador386.ordering.dto.OrderRequest;
import com.Silvador386.ordering.model.Order;
import com.Silvador386.ordering.model.OrderLineItems;
import com.Silvador386.ordering.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                                    .map(OrderLineItems::getSkuCode)
                                    .toList();

        /* Call InventoryService and place and order if product is in stock. */
        InventoryResponse[] inventoryResponses = webClient.get()
                 .uri("http://localhost:8082/api/inventory",
                         uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                 .retrieve()
                 .bodyToMono(InventoryResponse[].class)
                 .block();

        boolean allInStock = Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::isInStock);

        if(allInStock){
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is currently out of stock!");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItems;
    }
}
