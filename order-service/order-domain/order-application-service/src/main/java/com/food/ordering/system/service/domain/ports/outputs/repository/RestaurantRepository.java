package com.food.ordering.system.service.domain.ports.outputs.repository;

import com.food.ordering.system.order.service.domain.entity.Restaurant;

import java.util.Optional;


public interface RestaurantRepository {
    //return name,price
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
