package com.svydovets.bibirnate.configuration;

import com.svydovets.bibirnate.configuration.properties.ConfigurationProperties;
import com.svydovets.bibirnate.exceptions.PropertiesFileInvalidException;
import com.svydovets.bibirnate.exceptions.PropertiesFileMissingException;
import com.svydovets.bibirnate.exceptions.PropertiesFileValidationException;

public interface ConfigurationPropertiesReader {

    /**
     * Read persistence properties from corresponding file.
     *
     * @param filename   - path to file in classpath
     * @return ConfigurationProperties - POJO class with configuration properties
     * @throws PropertiesFileMissingException in case when properties file is missing
     * @throws PropertiesFileInvalidException in case when properties file is invalid
     * @throws PropertiesFileValidationException  in case when properties file has invalid field values
     */
    ConfigurationProperties readProperties(String filename);
}
