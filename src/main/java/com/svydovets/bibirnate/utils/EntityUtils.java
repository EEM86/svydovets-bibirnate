package com.svydovets.bibirnate.utils;

import java.lang.reflect.Field;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;

public final class EntityUtils {

    private EntityUtils() {
    }

    public static boolean isRegularField(Field field) {
        return !isEntityCollectionField(field) && !isEntityField(field);
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
