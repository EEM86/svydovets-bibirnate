package com.svydovets.bibirnate.mapper.converter;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class FloatConverter implements SqlDataTypeConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(Object value, Field field) {
        var val = (Float) value;
        if (field.getType().equals(BigDecimal.class)) {
            return BigDecimal.valueOf(val);
        }
        return val;
    }
}
