package com.food.ordering.system.service.domain.ports;

import com.food.ordering.system.order.service.domain.service.OrderDomainService;
import com.food.ordering.system.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.service.domain.ports.outputs.repository.CustomerRepository;
import com.food.ordering.system.service.domain.ports.outputs.repository.OrderRepository;
import com.food.ordering.system.service.domain.ports.outputs.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){

    }
}
