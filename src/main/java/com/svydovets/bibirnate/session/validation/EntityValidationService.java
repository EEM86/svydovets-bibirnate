package com.svydovets.bibirnate.session.validation;

import java.util.Arrays;
import java.util.Objects;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.JoinColumn;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.OneToOne;
import com.svydovets.bibirnate.exceptions.EntityValidationException;
import com.svydovets.bibirnate.session.query.ToOneRelation;

/**
 * Class that contains different validations for Entity.
 */
public class EntityValidationService {

    /**
     * Check that provided entity is annotated with @Entity.
     * and contains a field annotated with @Id.
     *
     * @param entity - object that should be validated
     */
    public <T> void validateEntity(T entity) {
        if (!entity.getClass().isAnnotationPresent(Entity.class)) {
            throw new EntityValidationException("Persistent Class should be annotated with @Entity annotation");
        }
        if (!entityHasId(entity)) {
            throw new EntityValidationException("Entity class should have id field annotated with @Id annotation");
        }
    }

    /**
     * Check that provided child object is annotated with @Entity and contains a field annotated with @Id.
     * Checks that a child object contains a field with a name equals to mappedBy value in parent.
     * Example:
     * class Parent{
     * OneToMany(mappedBy = "parent")
     * List Child childRelations;
     * }
     * class Child {
     * ManyToOne
     * JoinColumn(name = parent_id)
     * Parent parent;
     * }
     *
     * @param entity   - object that should be validated
     * @param mappedBy - value from the Parent's class mappedBy mapping
     */
    public <T> void validateChildEntity(T entity, String mappedBy) {
        validateEntity(entity);
        var hasChildMapping = Arrays.stream(entity.getClass().getDeclaredFields())
          .anyMatch(field -> field.getName().equals(mappedBy));
        if (!hasChildMapping) {
            throw new EntityValidationException("Child entity : %s doesn't have mapping to parent entity");
        }
    }

    public <T> void validateToOneEntity(T entity, ToOneRelation relation) {
        validateEntity(entity);
        var fieldName = relation.getField().getName();
        var childClass = relation.getRelatedEntity().getClass();
        if (relation.getField().isAnnotationPresent(ManyToOne.class)) {
            var hasParentMapping = Arrays.stream(childClass.getDeclaredFields())
              .filter(field -> field.isAnnotationPresent(OneToMany.class))
              .anyMatch(field -> Objects.nonNull(field.getAnnotation(OneToMany.class).mappedBy())
                && field.getAnnotation(OneToMany.class).mappedBy().equals(fieldName));
            if (!relation.getField().isAnnotationPresent(JoinColumn.class) || !hasParentMapping) {
                throw new EntityValidationException(
                  String.format("Incorrect @ManyToOne mapping for field: %s in class %s. "
                      + "@ManyToOne field should contain @JoinColumn mapping, "
                      + "field in related entity should be annotated with "
                      + "@OneToMany(mappedBy=\"<ManyToOne field name>\")",
                    relation.getField().getName(),
                    relation.getField().getDeclaringClass().getName()));
            }
        } else {
            if (relation.getField().isAnnotationPresent(JoinColumn.class)) {
                var hasParentMapping = Arrays.stream(childClass.getDeclaredFields())
                  .filter(field -> field.isAnnotationPresent(OneToOne.class))
                  .anyMatch(field -> Objects.nonNull(field.getAnnotation(OneToOne.class).mappedBy())
                    && field.getAnnotation(OneToOne.class).mappedBy().equals(fieldName));
                if (!relation.getField().isAnnotationPresent(JoinColumn.class) || !hasParentMapping) {
                    throw new EntityValidationException(
                      String.format("Incorrect @OneToOne mapping for field: %s in class %s. "
                          + "@OneToOne field should contain @JoinColumn mapping, "
                          +
                          "field in related entity should be annotated with "
                          + "@OneToMany(mappedBy=\"<OneToOne child field name>\")",
                        relation.getField().getName(),
                        relation.getField().getDeclaringClass().getName()));
                }
            } else {
                var mappedByVal = relation.getMappedBy();
                var hasMapping = Arrays.stream(childClass.getDeclaredFields())
                  .filter(
                    field -> field.isAnnotationPresent(OneToOne.class) && field.isAnnotationPresent(JoinColumn.class))
                    .anyMatch(field -> field.getName().equals(mappedByVal));
                if (!hasMapping) {
                    throw new EntityValidationException(
                      String.format("Incorrect @OneToOne mapping for field: %s in class %s. "
                          + "@OneToOne field should contain mappedBy = \"value\" where value refers to a name "
                          + "of backward OneToOne mapping"
                          + "Field in related entity should be annotated with "
                          + "@OneToMany and @JoinColumn" ,
                        relation.getField().getName(),
                        relation.getField().getDeclaringClass().getName()));
                }
            }
        }
    }

    /**
     * Check that provided entity has a field annotated with @Id.
     *
     * @param entity - object that should be validated
     */
    public <T> boolean entityHasId(T entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
          .anyMatch(field -> field.isAnnotationPresent(Id.class));
    }
}
