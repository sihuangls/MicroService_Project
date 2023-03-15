package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final Money price;
    private final StreetAddress deliveryAddress;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;


    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay(){
        if(orderStatus != orderStatus.PENDING){
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }
        orderStatus = orderStatus.PAID;
    }

    public void approved(){
        if(orderStatus != orderStatus.PAID){
            throw new OrderDomainException("Order is not in correct state for approved operation!");
        }
        orderStatus = orderStatus.APPROVED;
    }
    //如果产生了失败信息，那么需要将其存储和打印出来
    public void initCancel(List<String> failureMessages){
        if(orderStatus != orderStatus.PAID){
            throw new OrderDomainException("Order is not in correct state for initCancel operation!");
        }
        orderStatus = orderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages){
        if(!(orderStatus == orderStatus.PAID || orderStatus == orderStatus.CANCELLING)){
            throw new OrderDomainException("Order is not in correct state for initCancel operation!");
        }
        orderStatus = orderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if(this.failureMessages != null && failureMessages != null){
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).collect(Collectors.toList()));
        }
        if(this.failureMessages == null){
            this.failureMessages = failureMessages;
        }
    }



    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO,Money::add);

        if(!price.equals(orderItemsTotal)){
            throw new OrderDomainException("Total price: " + price.getAmount() + " is not equal to Order items total: " + orderItemsTotal.getAmount() + "!");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValid()){
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() + " is not valid for product " + orderItem.getProduct().getId().getValue());
        }
    }

    private void validateTotalPrice() {
        if(price == null || !price.isGreaterThanZero()){
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    //getId继承自父类
    private void validateInitialOrder() {
        if(orderStatus != null || getId() != null){
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    public void initializeOrderItems(){
        long itemId = 1;
        for(OrderItem orderItem : items){
            orderItem.initializeOrderItems(super.getId(), new OrderItemId(itemId++));
        }
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        price = builder.price;
        deliveryAddress = builder.deliveryAddress;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }


    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public Money getPrice() {
        return price;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public List<OrderItem> getItem() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private Money price;
        private StreetAddress deliveryAddress;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
