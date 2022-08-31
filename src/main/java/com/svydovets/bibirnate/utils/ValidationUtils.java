package com.svydovets.bibirnate.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.svydovets.bibirnate.configuration.NotNull;
import com.svydovets.bibirnate.exceptions.PropertiesFileValidationException;

public class ValidationUtils {

    public static <T> void validateNotNullFields(Class<T> entityType, T instance) {
        List<Field> collect = Arrays.stream(entityType.getDeclaredFields()).peek(f -> f.setAccessible(true))
                .filter(f -> {
                    try {
                        return f.isAnnotationPresent(NotNull.class) && f.get(instance) == null;
                    } catch (IllegalAccessException ex) {
                        throw new PropertiesFileValidationException(ex);
                    }
                }).toList();
        if (!collect.isEmpty()) {
            throw new PropertiesFileValidationException(String.join(",",
              collect.stream().map(Field::getName).toList()));
        }
    }
}
