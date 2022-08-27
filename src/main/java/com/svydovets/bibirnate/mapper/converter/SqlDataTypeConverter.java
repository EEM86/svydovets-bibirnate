package com.svydovets.bibirnate.mapper.converter;

import java.lang.reflect.Field;

/**
 * Interface responsible for converting database result value to specific java data type.
 */
public interface SqlDataTypeConverter {

    /**
     * Converts and set SQL result value to corresponding java Object.
     *
     * @param value - value returned from database
     * @param field - entity field database value should be mapped to
     */
    Object convert(Object value, Field field);


}
