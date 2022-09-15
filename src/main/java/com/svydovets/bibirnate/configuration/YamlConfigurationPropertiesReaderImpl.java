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

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of @ConfigurationPropertiesReader to read configuration properties from YAML file.
 */
@Slf4j
public class YamlConfigurationPropertiesReaderImpl implements ConfigurationPropertiesReader {

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigurationProperties readProperties(String filename) {
        log.info("Start to parse and validate yaml properties from file [{}]...", filename);
        var yaml = new Yaml(new Constructor(ConfigurationProperties.class));
        try (var inputStream = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new PropertiesFileMissingException(filename);
            }
            var properties = (ConfigurationProperties) yaml.load(inputStream);
            if (properties == null || properties.getDatabase() == null) {
                throw new PropertiesFileValidationException("Database");
            }
            ValidationUtils.validateNotNullDatabaseFields(DatabaseProperties.class, properties.getDatabase());
            log.info("Finish parse and validate .yaml properties from file [{}].", filename);
            return properties;
        } catch (YAMLException ex) {
            log.error("Cannot parse .yaml properties from the file [{}]. Failed with exception [{}].", filename,
              ex.getMessage());
            throw new PropertiesFileInvalidException(filename);
        } catch (IOException ex) {
            log.error("Cannot parse .yaml properties from the file [{}]. Failed with exception [{}].", filename,
              ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
