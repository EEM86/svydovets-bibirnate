package com.svydovets.bibirnate.exceptions;

/**
 * Exception is thrown if configuration properties values fails @NotNull validation
 */

public class PropertiesFileValidationException extends RuntimeException {

    public PropertiesFileValidationException(String invalidProperties) {
        super(String.format("[%s] persistence properties should not be null.", invalidProperties));
    }

    public PropertiesFileValidationException(Throwable cause) {
        super("Failed during validation of configuration properties file", cause);
    }
}
