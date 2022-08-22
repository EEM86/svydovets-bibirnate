package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.svydovets.bibirnate.exceptions.TypeMismatchException;


public class RegularFieldMapper implements EntityFieldMapper {

    @Override
    public <T> void mapField(Field field, T instance, Object value) {
        try {
            field.setAccessible(true);
            if (value instanceof Timestamp timestamp) {
//                todo: check on all Date types
                value = timestamp.toLocalDateTime();
            } else if (value instanceof Blob blob) {
                try {
                    var len = blob.length();
                    value = blob.getBytes(1, (int) len);
                } catch (SQLException e) {
                    throw new RuntimeException("Error reading Blob from database", e);
                }
            }
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Could not access field: %s while mapping entity: %s",
              field.getName(), instance.getClass().getSimpleName()), e);
        } catch (IllegalArgumentException e) {
            throw new TypeMismatchException(
              String.format("Database type: %s doesn't match provided entity type: %s",
                value.getClass().getSimpleName(), field.getType().getSimpleName()), e);
        }
    }

//    private void setDateTimeField(Class<?> fieldType) {
//
//    }

}
