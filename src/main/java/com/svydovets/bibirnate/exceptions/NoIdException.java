package com.svydovets.bibirnate.exceptions;

import com.svydovets.bibirnate.annotation.Id;

/**
 * Exception is thrown if entity class does not have any field marked with {@link Id}.
 */
public class NoIdException extends RuntimeException {
    public NoIdException(Class<?> entity) {
        super(String.format("%s does not have any field marked with @Id", entity.getSimpleName()));
    }
}
