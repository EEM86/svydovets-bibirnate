package com.svydovets.bibirnate.exceptions;

import com.svydovets.bibirnate.annotation.Id;

/**
 * Exception is thrown if entity class has several fields marked with {@link Id}.
 */
public class AmbiguousIdException extends RuntimeException {
    public AmbiguousIdException(Class<?> entity) {
        super(String.format("%s has field several fields marked with @Id", entity.getSimpleName()));
    }
}
