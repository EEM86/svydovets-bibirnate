package com.svydovets.bibirnate.session.state.key;

import static com.svydovets.bibirnate.utils.EntityUtils.getIdField;

import java.util.Objects;

import com.svydovets.bibirnate.exceptions.PersistenceException;


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
