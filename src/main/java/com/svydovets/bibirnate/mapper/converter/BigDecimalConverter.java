package com.svydovets.bibirnate.mapper.converter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalConverter implements SqlDataTypeConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(Object value, Field field) {
        var val = (BigDecimal) value;
        if (field.getType().equals(BigInteger.class)) {
            return val.toBigInteger();
        }

        if (field.getType().equals(Long.class)) {
            return val.longValue();
        }

        if (field.getType().equals(Integer.class)) {
            return val.intValue();
        }
        
        return val;
    }
}
