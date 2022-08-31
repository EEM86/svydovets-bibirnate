package com.svydovets.bibirnate.exceptions;


/**
 * Exception is thrown if configuration properties file is can't be parsed due to invalid properties
 * or invalid YAML structure.
 */

public class PropertiesFileInvalidException extends RuntimeException {

    public PropertiesFileInvalidException(String fileName) {
        super(String.format("Persistence properties configuration file('%s') is invalid. Please, refer to Bibernate documentation and YAML specification: https://yaml.org/spec/", fileName));
    }
}
