package com.svydovets.bibirnate.cache.key.parameters;

import java.util.Objects;

/**
 * This class is extension for the {@link AbstractKeyParam} that builds on the entity class type and entity id.
 */
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntityKeyParam<?> that)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

}
