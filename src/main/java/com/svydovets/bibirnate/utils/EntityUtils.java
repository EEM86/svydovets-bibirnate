package com.svydovets.bibirnate.utils;

import java.lang.reflect.Field;
import java.util.Collection;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.OneToOne;

/**
 * Utility class with different entity-related helper methods.
 */
public final class EntityUtils {

    private EntityUtils() {
    }

    /**
     * Checks if the entity field is of standard java type or primitive.
     *
     * @param field - mapped entity's field.
     * @return true if the field is a regular java type field, otherwise - false
     */
    public static boolean isRegularField(Field field) {
        boolean simpleField = !isEntityCollectionField(field) && !isEntityField(field)
          && !Collection.class.isAssignableFrom(field.getType())
          && isJavaType(field);
        if (!simpleField && field.getType().isArray()) {
            simpleField = field.getType().componentType().equals(byte.class);
        }
        return simpleField;
    }

    /**
     * Checks if the entity field is a custom object with any to one relations.
     *
     * @param field - mapped entity's field.
     * @return true if the field is a custom object, otherwise - false
     */
    public static boolean isEntityField(Field field) {
        return field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class);
    }

    /**
     * Checks if the entity field is a Collection object with any to many relations.
     *
     * @param field - mapped entity's field
     * @return true if the field is a collection field, otherwise - false
     */
    public static boolean isEntityCollectionField(Field field) {
        return field.isAnnotationPresent(OneToMany.class);
    }

    /**
     * Returns the field name either specified with a {@link Column} annotation or the same as an entity's field name.
     *
     * @param field - mapped entity's field
     */
    public static String getFieldName(Field field) {
        return field.isAnnotationPresent(Column.class)
            && !field.getAnnotation(Column.class).name().isEmpty()
            ? field.getAnnotation(Column.class).name() : field.getName();
    }

    private static boolean isJavaType(Field field) {
        return field.getType().isPrimitive()
            || field.getType().getName().startsWith("java");
    }
}
