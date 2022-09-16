package com.svydovets.bibirnate.session.validation;

import java.util.Arrays;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.exceptions.EntityValidationException;

public class ValidationService {

    public <T> void validateEntity(T entity) {
        if (!entity.getClass().isAnnotationPresent(Entity.class)) {
            throw new EntityValidationException("Persistent Class should be annotated with @Entity annotation");
        }
        if (!entityHasId(entity)) {
            throw new EntityValidationException("Entity class should have id field annotated with @Id annotation");
        }
    }

    public <T> void validateChildEntity(T entity, String mappedBy) {
        validateEntity(entity);
        var hasChildMapping = Arrays.stream(entity.getClass().getDeclaredFields())
          .anyMatch(field -> field.getName().equals(mappedBy));
        if (!hasChildMapping) {
            throw new EntityValidationException("Child entity : %s doesn't have mapping to parent entity");
        }
    }

    public <T> boolean entityHasId(T entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
          .anyMatch(field -> field.isAnnotationPresent(Id.class));
    }
}
