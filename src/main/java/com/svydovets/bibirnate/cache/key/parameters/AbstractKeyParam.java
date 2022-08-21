package com.svydovets.bibirnate.cache.key.parameters;

import java.util.Objects;

public abstract class AbstractKeyParam<T> {

    private final Class<T> entityType;

    public AbstractKeyParam(Class<T> entityType) {
        this.entityType = entityType;
    }

    public Class<T> getEntityType() {
        return entityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractKeyParam)) return false;
        AbstractKeyParam<?> that = (AbstractKeyParam<?>) o;
        return getEntityType().equals(that.getEntityType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntityType());
    }
}
