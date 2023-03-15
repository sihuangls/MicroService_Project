package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public class Productid extends BaseId<UUID>{

    protected Productid(UUID value) {
        super(value);
    }
}
