package com.svydovets.bibirnate.exceptions;

/**
 * Exception is thrown if configuration properties values fails @NotNull validation.
 */

public class PropertiesFileValidationException extends RuntimeException {

    private static final String EMPTY_PROPERTIES_MESSAGE = "[%s] persistence properties should not be null. "
      + "Please provide properties for database connection. Use YAML file or provide through "
      + "DefaultSessionFactoryBuilderImpl.withDatabaseProperties.";

    public static final String GENERIC_FAILURE_MESSAGE = "Failed during validation of configuration properties file";

    public PropertiesFileValidationException(String invalidProperties) {
        super(String.format(EMPTY_PROPERTIES_MESSAGE, invalidProperties));
    }

    public PropertiesFileValidationException(Throwable cause) {
        super(GENERIC_FAILURE_MESSAGE, cause);
    }
}
