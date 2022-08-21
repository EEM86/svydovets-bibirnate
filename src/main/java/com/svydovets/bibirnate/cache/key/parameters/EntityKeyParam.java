package com.svydovets.bibirnate.cache.key.parameters;

import java.util.Objects;

public class EntityKeyParam<T> extends AbstractKeyParam<T> {

    private final Object id;

    public EntityKeyParam(Class<T> entityType, Object id) {
        super(entityType);
        this.id = id;
    }

    public Object getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityKeyParam)) return false;
        if (!super.equals(o)) return false;
        EntityKeyParam<?> that = (EntityKeyParam<?>) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }
}
