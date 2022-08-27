package com.svydovets.bibirnate.mapper.converter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IntConverter implements SqlDataTypeConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(Object value, Field field) {
        Integer val = (Integer) value;
        if (field.getType().equals(Short.class) || field.getType().equals(short.class)) {
            return val.shortValue();
        } else if (field.getType().equals(BigInteger.class)) {
            return BigInteger.valueOf(val);
        }
        return val;
    }
}
