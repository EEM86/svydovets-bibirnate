package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;

public interface EntityFieldMapper {

    <T> void mapField(Field field, T instance, Object value);
}
