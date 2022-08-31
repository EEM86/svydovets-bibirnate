package com.svydovets.bibirnate.exceptions;


/**
 * Exception is thrown if configuration properties file missing in classpath
 */
public class PropertiesFileMissingException extends RuntimeException {

    public PropertiesFileMissingException(String fileName) {
        super(String.format("Persistence properties configuration file('%s') is missing in resource folder.", fileName));
    }
}
