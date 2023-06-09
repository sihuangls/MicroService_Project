package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.Productid;

public class Product extends BaseEntity<Productid> {
    String name;
    Money price;

    public Product(Productid productid, String name, Money price) {
        super.setId(productid);
        this.name = name;
        this.price = price;
    }
    public Product(Productid productid){
        super.setId(productid);
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
