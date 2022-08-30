package com.svydovets.bibirnate.mapper.converter;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class TimestampConverter implements SqlDataTypeConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(Object value, Field field) {
        var val = (Timestamp) value;
        if (field.getType().equals(LocalDate.class)) {
            return val.toLocalDateTime().toLocalDate();
        } else if (field.getType().equals(LocalDateTime.class)) {
            return val.toLocalDateTime();
        } else if (field.getType().equals(Date.class)) {
            return new Date(val.getTime());
        }
        return val;
    }

}
