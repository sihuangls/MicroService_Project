package com.food.ordering.system.order.service.dataaccess.order.adapter;

import com.food.ordering.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.order.repository.OrderJpaResposity;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import com.food.ordering.system.service.domain.ports.outputs.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRespositoryImpl implements OrderRepository {

    private final OrderJpaResposity orderJpaResposity;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRespositoryImpl(OrderJpaResposity orderJpaResposity,
                                OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJpaResposity = orderJpaResposity;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(orderJpaResposity
                .save(orderDataAccessMapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaResposity.findByTrackingId(trackingId.getValue()).map(orderDataAccessMapper::orderEntityToOrder);
    }
}
