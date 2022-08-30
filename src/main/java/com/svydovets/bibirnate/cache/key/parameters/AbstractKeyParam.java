package com.svydovets.bibirnate.cache.key.parameters;

import java.util.Objects;

/**
 * This is an abstraction for key parameter instances.
 */
public abstract class AbstractKeyParam<T> {

    private final Class<T> entityType;

    public AbstractKeyParam(Class<T> entityType) {
        this.entityType = entityType;
    }

    public Class<T> getEntityType() {
        return entityType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractKeyParam<?> that)) {
            return false;
        }
        return getEntityType().equals(that.getEntityType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntityType());
    }

}
