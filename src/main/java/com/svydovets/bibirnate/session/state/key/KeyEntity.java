package com.svydovets.bibirnate.session.state.key;

import java.util.Objects;

import static com.svydovets.bibirnate.utils.EntityUtils.getIdField;


public record KeyEntity(Class<?> entityType, Object id) {

    public static KeyEntity of(Object entity) {
        Objects.requireNonNull(entity);
        var type = entity.getClass();
        KeyEntity keyEntity = null;
        try {
            var idField = getIdField(entity.getClass());
            idField.setAccessible(Boolean.TRUE);
            var id = idField.get(entity);
            keyEntity = new KeyEntity(type, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return keyEntity;
    }
}
