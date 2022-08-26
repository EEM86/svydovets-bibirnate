package com.svydovets.bibirnate.mapper.converter;

import java.lang.reflect.Field;

public class DefaultConverter implements SqlDataTypeConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(Object value, Field field) {
        return value;
    }
}
