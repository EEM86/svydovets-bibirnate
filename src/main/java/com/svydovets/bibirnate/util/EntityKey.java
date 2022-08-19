package com.svydovets.bibirnate.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
public class EntityKey<T> {

    public EntityKey(Object id) {
        this.id = id;
    }

    public EntityKey(Class<T> entityType) {
        this.entityType = entityType;
    }

    public EntityKey(Class<T> entityType, Object id) {
        this.entityType = entityType;
        this.id = id;
    }

    private Class<T> entityType;
    private Object id;

    public Class<T> getEntityType() {
        return entityType;
    }

    public Object getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityKey<?> entityKey = (EntityKey<?>) o;
        return entityType.equals(entityKey.entityType) && id.equals(entityKey.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityType, id);
    }
}
