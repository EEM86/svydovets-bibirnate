package com.svydovets.bibirnate.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Predicate;

import com.svydovets.bibirnate.configuration.NotNull;
import com.svydovets.bibirnate.exceptions.PropertiesFileValidationException;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static <T> void validateNotNullDatabaseFields(Class<T> entityType, T instance) {
        var collect = Arrays.stream(entityType.getDeclaredFields())
                .peek(AccessibleObject::trySetAccessible)
                .filter(checkNonNullableFieldsAreNotNull(instance))
                .toList();
        if (!collect.isEmpty()) {
            throw new PropertiesFileValidationException(String.join(",",
              collect.stream().map(Field::getName).toList()));
        }
    }

    private static <T> Predicate<Field> checkNonNullableFieldsAreNotNull(T instance) {
        return f -> {
            try {
                return f.isAnnotationPresent(NotNull.class) && f.get(instance) == null;
            } catch (IllegalAccessException ex) {
                throw new PropertiesFileValidationException(ex);
            }
        };
    }
}
