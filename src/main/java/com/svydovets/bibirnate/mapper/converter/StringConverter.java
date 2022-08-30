package com.svydovets.bibirnate.mapper.converter;

import java.lang.reflect.Field;

public class StringConverter implements SqlDataTypeConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(Object value, Field field) {
        var val = (String) value;
        if (field.getType().equals(Character.class) || field.getType().equals(char.class)) {
            return val.charAt(0);
        }
        return val;
    }
}
