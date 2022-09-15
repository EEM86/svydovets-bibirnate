package com.svydovets.bibirnate.session.impl.builder;

import com.svydovets.bibirnate.configuration.YamlConfigurationPropertiesReaderImpl;
import com.svydovets.bibirnate.session.SessionFactory;

/**
 * Generates new {@link SessionFactory} from .yaml configuration file.
 */
public class YamlConfigurationSessionFactoryBuilderImpl extends AbstractSessionFactoryBuilderImpl
  implements SessionFactoryBuilder {

    private String filename;

    public YamlConfigurationSessionFactoryBuilderImpl withFilename(String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionFactory build() {
        var configurationProperties = new YamlConfigurationPropertiesReaderImpl().readProperties(filename);
        return buildSessionFactory(configurationProperties);
    }
}
