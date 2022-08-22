package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.svydovets.bibirnate.exceptions.EntityMappingException;
import com.svydovets.bibirnate.exceptions.TypeMismatchException;


public class RegularFieldMapper implements EntityFieldMapper {

    @Override
    public <T> void mapField(Field field, T instance, Object value) {
        try {
            field.setAccessible(true);
            value = defineValue(value, field);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new EntityMappingException(String.format("Could not access field: %s while mapping entity: %s",
              field.getName(), instance.getClass().getSimpleName()), e);
        } catch (IllegalArgumentException e) {
            throw new TypeMismatchException(
              String.format("Database type: %s doesn't match provided entity type: %s",
                value.getClass().getSimpleName(), field.getType().getSimpleName()), e);
        }
    }

    private Object defineValue(Object value, Field field) {
        if (value instanceof Integer intVal && field.getType().equals(Short.class)) {
            value = intVal.shortValue();
        } else if (value instanceof BigDecimal bdVal && field.getType().equals(BigInteger.class)) {
            value = bdVal.toBigInteger();
        } else if (value instanceof Timestamp timestamp) {
            value = setTimestampField(field, timestamp);
        } else if (value instanceof Blob blob) {
            try {
                var len = blob.length();
                value = blob.getBytes(1, (int) len);
            } catch (SQLException e) {
                throw new EntityMappingException("Error reading Blob from database", e);
            }
        } else if (value instanceof String strVal && field.getType().equals(Character.class)) {
            value = strVal.charAt(0);
        }
        return value;
    }

    private Object setTimestampField(Field field, Timestamp timestamp) {
        if (field.getType().equals(LocalDate.class)) {
            return timestamp.toLocalDateTime().toLocalDate();
        } else if (field.getType().equals(LocalDateTime.class)) {
            return timestamp.toLocalDateTime();
        } else if (field.getType().equals(Date.class)) {
            return new Date(timestamp.getTime());
        }
        return timestamp;
    }

}
