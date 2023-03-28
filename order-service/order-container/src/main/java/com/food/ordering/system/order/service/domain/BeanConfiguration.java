package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.service.OrderDomainService;
import com.food.ordering.system.order.service.domain.service.OrderDomainServiceImpl;
import org.springframework.context.annotation.Bean;

public class BeanConfiguration {

    @Bean
    public OrderDomainService orderDomainService(){
        return new OrderDomainServiceImpl();
    }
}
