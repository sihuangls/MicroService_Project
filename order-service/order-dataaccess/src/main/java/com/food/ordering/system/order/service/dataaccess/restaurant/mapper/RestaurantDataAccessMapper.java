package com.food.ordering.system.order.service.dataaccess.restaurant.mapper;


import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.Productid;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant){
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities){
        //先看又没有，没有就扔exception
        RestaurantEntity restaurantEntity =
                restaurantEntities.stream().findFirst().orElseThrow(() -> new RestaurantDataAccessException("Restaurant cannot be found!"));

        List<Product> restaurantProducts = restaurantEntities.stream()
                .map(entity -> new Product(new Productid(entity.getProductId()), entity.getProductName(), new Money(entity.getProductPrice())))
                .toList();

        //因为我们有一堆的productid和一个restaurantId，所以我们先从第一个把restaurantId拉出来
        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }

}
