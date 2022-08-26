package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;

public class ToOneFieldMapper implements EntityFieldMapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void mapField(Field field, T instance, Object value) {
        //        todo: possible solution
        //        todo: resultSet = execute request;
        //        todo: var innerObject = mapToObject(field.getType(), resultSet);
        //        todo: field.setAccessible(true);
        //        todo: field.set(instance, innerObject);
        System.out.println("not yet supported");
    }
}
