package com.svydovets.bibirnate.configuration;

import com.svydovets.bibirnate.exceptions.PropertiesFileInvalidException;
import com.svydovets.bibirnate.exceptions.PropertiesFileMissingException;
import com.svydovets.bibirnate.exceptions.PropertiesFileValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class YamlConfigurationPropertiesReaderImplTest {

    private final YamlConfigurationPropertiesReaderImpl reader = new YamlConfigurationPropertiesReaderImpl();

    private ConfigurationProperties configurationProperties;

    @Test
    public void testReadYamlPropertiesSuccess() {
        configurationProperties = reader.readProperties("persistence.yaml");
        assertEquals("url", configurationProperties.getDatabaseUrl());
        assertEquals("user", configurationProperties.getDatabaseUser());
        assertEquals("pass", configurationProperties.getDatabasePassword());
        assertEquals("databaseDriver", configurationProperties.getDatabaseDriverName());
    }

    @Test
    public void testReadYamlPropertiesThrowsPropertiesFileMissingException() {
        Assertions.assertThrows(PropertiesFileMissingException.class, () -> configurationProperties = reader.readProperties("non-existing-file.yaml"));
    }

    @Test
    public void testReadYamlPropertiesThrowsInvalidFileException() {
        Assertions.assertThrows(PropertiesFileInvalidException.class, () -> configurationProperties = reader.readProperties("persistence-not-valid.yaml"));
    }

    @Test
    public void testReadYamlPropertiesThrowsValidationException() {
        Assertions.assertThrows(PropertiesFileValidationException.class, () -> configurationProperties = reader.readProperties("persistence-missing-property.yaml"));
    }
}
