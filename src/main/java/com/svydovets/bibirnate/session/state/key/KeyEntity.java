package com.svydovets.bibirnate.session.state.key;

import com.svydovets.bibirnate.exceptions.PersistenceException;

import java.util.Objects;

import static com.svydovets.bibirnate.utils.EntityUtils.getIdField;


public record KeyEntity(Class<?> entityType, Object id) {

    public static KeyEntity of(Object entity) {
        Objects.requireNonNull(entity);
        var type = entity.getClass();
        KeyEntity keyEntity;
        var idField = getIdField(entity.getClass());
        try {
            idField.setAccessible(Boolean.TRUE);
            var id = idField.get(entity);
            keyEntity = new KeyEntity(type, id);
        } catch (IllegalAccessException ex) {
            throw new PersistenceException(String.format("Could not take a value of field: %s", idField.getName()), ex);
        }
        return keyEntity;
    }
}
