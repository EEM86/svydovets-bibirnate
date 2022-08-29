package com.svydovets.bibirnate.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.OneToOne;
import com.svydovets.bibirnate.annotation.Table;
import com.svydovets.bibirnate.exceptions.AmbiguousIdException;
import com.svydovets.bibirnate.exceptions.NoIdException;

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
          && !Collection.class.isAssignableFrom(field.getType()) && isJavaType(field);
        if (!simpleField && field.getType().isArray()) {
            simpleField = field.getType().componentType().equals(byte.class);
        }
        return simpleField;
    }

    /**
     * Checks if the entity field is a custom object with any to one relation.
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
        return Optional.ofNullable(field.getAnnotation(Column.class))
          .map(Column::name)
          .filter(Predicate.not(String::isEmpty))
          .orElse(field.getName());
    }

    private static boolean isJavaType(Field field) {
        return field.getType().isPrimitive()
            || field.getType().getName().startsWith("java");
    }

    /**
     * Return field marked with annotation {@link Id}.
     * If entity does not have annotation {@link Id} {@link NoIdException} will be thrown.
     * if entity has more than one field annotated with {@link Id} {@link AmbiguousIdException} will be thrown.
     *
     * @param entityType - entity class
     */
    public static <T> Field getIdField(Class<T> entityType) {
        return Arrays.stream(entityType.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Id.class))
          .collect(Collectors.collectingAndThen(Collectors.toList(), getOneFieldCollector(entityType)));
    }

    /**
     * Returns the table name either specified with a {@link Table} annotation or the same as an entity's class name.
     */
    public static <T> String getTableName(Class<T> entityType) {
        return Optional.ofNullable(entityType.getAnnotation(Table.class)).map(Table::name)
          .filter(Predicate.not(String::isEmpty)).orElse(entityType.getSimpleName());
    }

    private static <T> Function<List<Field>, Field> getOneFieldCollector(Class<T> entityType) {
        return list -> {
            if (list.size() == 0) {
                throw new NoIdException(entityType);
            }
            if (list.size() > 1) {
                throw new AmbiguousIdException(entityType);
            }
            return list.get(0);
        };
    }
}
