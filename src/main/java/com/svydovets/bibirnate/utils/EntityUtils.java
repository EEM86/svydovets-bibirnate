package com.svydovets.bibirnate.utils;

import java.lang.reflect.Field;
import java.util.Collection;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;

public final class EntityUtils {

    private EntityUtils() {
    }

    public static boolean isRegularField(Field field) {
        boolean simpleField = !isEntityCollectionField(field) && !isEntityField(field) &&
          !Collection.class.isAssignableFrom(field.getType()) &&
          isJavaType(field);
        if (!simpleField && field.getType().isArray()) {
            simpleField = field.getType().componentType().equals(byte.class);
        }
        return simpleField;
    }

    private static boolean isJavaType(Field field) {
        return field.getType().getName().startsWith("java.lang") ||
          field.getType().getName().startsWith("java.math") ||
          field.getType().getName().startsWith("java.util") ||
          field.getType().getName().startsWith("java.sql") ||
          field.getType().getName().startsWith("java.time");
    }

    public static boolean isEntityField(Field field) {
        return field.isAnnotationPresent(ManyToOne.class);
    }

    public static boolean isEntityCollectionField(Field field) {
        return field.isAnnotationPresent(OneToMany.class);
    }

    public static String getFieldName(Field field) {
        return field.isAnnotationPresent(Column.class) && !field.getAnnotation(Column.class).name().isEmpty() ?
          field.getAnnotation(Column.class).name() : field.getName();
    }
}
