package com.food.ordering.system.domain.valueobject;

import java.util.Objects;

public abstract class BaseId<T> {
    private final T value;

    //reaching the subclass is enough
    //shouldn't use public
    protected BaseId(T value) {
        this.value = value;
    }

    public T getValue(){
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return value.equals(baseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
