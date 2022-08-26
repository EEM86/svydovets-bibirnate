package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;

/**
 * Interface responsible for converting SQL result to Object and vica verse.
 */
public interface EntityFieldMapper {

    /**
     * Converts and set SQL field's result value to the field of a created instance of the Object.
     *
     * @param field - mapped entity field that should be set with the value from the database.
     * @param instance - result object's instance.
     * @param value - database result set field's value.
     */
    <T> void mapField(Field field, T instance, Object value);
}
