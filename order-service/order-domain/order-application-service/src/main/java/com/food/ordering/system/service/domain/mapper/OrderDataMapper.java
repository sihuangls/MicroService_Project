package com.food.ordering.system.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.Productid;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.service.domain.dto.create.OrderAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand){
        //我们发现product并不一定需要名字和price，有时候只需要一个充满productId的restaurant
        //所以我们去Product中override一个新的
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream().map(orderItem ->
                        new Product(new Productid(orderItem.getProductId())))
                        .collect(Collectors.toList()))
                .build();
    }

    //因为Order中是streetAddress,是具备额外id的，所以我们需要转置我们的orderAddress
    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand){
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.getItems()))
                .build();
    }
    //这个部分我们并不设置id，逻辑仅仅设置我们的item
    //因为这个事customer会交予我们的信息
    private List<OrderItem> orderItemsToOrderItemEntities(List<com.food.ordering.system.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem ->
                        OrderItem.builder()
                                .setProduct(new Product(new Productid(orderItem.getProductId())))
                                .setPrice(new Money(orderItem.getPrice()))
                                .setQuantity(orderItem.getQuantity())
                                .setSubTotal(new Money(orderItem.getSubTotal()))
                                .build()).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderaddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderaddress.getStreet(),
                orderaddress.getPostalCode(),
                orderaddress.getCity()
        );
    }

    //这里并不需要返回任何信息，只需要给予response
    public CreateOrderResponse orderToCreateOrderResponse(Order order){
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
