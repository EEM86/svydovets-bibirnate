package com.svydovets.bibirnate.configuration;

import java.io.IOException;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;

import com.svydovets.bibirnate.configuration.properties.ConfigurationProperties;
import com.svydovets.bibirnate.configuration.properties.DatabaseProperties;
import com.svydovets.bibirnate.exceptions.PropertiesFileInvalidException;
import com.svydovets.bibirnate.exceptions.PropertiesFileMissingException;
import com.svydovets.bibirnate.exceptions.PropertiesFileValidationException;
import com.svydovets.bibirnate.utils.ValidationUtils;

/**
 * Implementation of @ConfigurationPropertiesReader to read configuration properties from YAML file.
 */
public class YamlConfigurationPropertiesReaderImpl implements ConfigurationPropertiesReader {

    @Override
    public ConfigurationProperties readProperties(String filename) {
        var yaml = new Yaml(new Constructor(ConfigurationProperties.class));
        try (var inputStream = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new PropertiesFileMissingException(filename);
            }
            var properties = (ConfigurationProperties) yaml.load(inputStream);
            if (properties == null || properties.getDatabase() == null) {
                throw new PropertiesFileValidationException("Database");
            }
            return properties;
        } catch (YAMLException ex) {
            throw new PropertiesFileInvalidException(filename);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
