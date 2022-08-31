package com.svydovets.bibirnate.configuration;


import com.svydovets.bibirnate.exceptions.PropertiesFileInvalidException;
import com.svydovets.bibirnate.exceptions.PropertiesFileMissingException;
import com.svydovets.bibirnate.utils.ValidationUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of @ConfigurationPropertiesReader to read configuration properties from YAML file
 * */
public class YamlConfigurationPropertiesReaderImpl implements ConfigurationPropertiesReader {

    @Override
    public ConfigurationProperties readProperties(String filename) {
        Yaml yaml = new Yaml(new Constructor(ConfigurationProperties.class));
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new PropertiesFileMissingException(filename);
            }
            ConfigurationProperties properties = yaml.load(inputStream);
            ValidationUtils.validateNotNullFields(ConfigurationProperties.class, properties);
            return properties;
        } catch (YAMLException e) {
            throw new PropertiesFileInvalidException(filename);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
