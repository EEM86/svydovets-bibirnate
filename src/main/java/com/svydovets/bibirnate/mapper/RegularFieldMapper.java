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
import com.svydovets.bibirnate.mapper.converter.DataConverterFactory;


public class RegularFieldMapper implements EntityFieldMapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void mapField(Field field, T instance, Object value) {
        try {
            field.setAccessible(true);
            var converter = DataConverterFactory.getConverter(value);
            value = converter.convert(value, field);
            field.set(instance, value);
        } catch (IllegalAccessException ex) {
            throw new EntityMappingException(String.format("Could not access field: %s while mapping entity: %s",
              field.getName(), instance.getClass().getSimpleName()), ex);
        } catch (IllegalArgumentException ex) {
            throw new TypeMismatchException(
              String.format("Database type: %s doesn't match provided entity type: %s",
                value.getClass().getSimpleName(), field.getType().getSimpleName()), ex);
        }
    }
}
