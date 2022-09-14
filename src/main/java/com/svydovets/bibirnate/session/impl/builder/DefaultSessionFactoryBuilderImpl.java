package com.svydovets.bibirnate.session.impl.builder;

import java.util.Objects;
import java.util.function.Supplier;

import com.svydovets.bibirnate.configuration.properties.CacheProperties;
import com.svydovets.bibirnate.configuration.properties.ConfigurationProperties;
import com.svydovets.bibirnate.configuration.properties.ConnectionPoolProperties;
import com.svydovets.bibirnate.configuration.properties.DatabaseProperties;
import com.svydovets.bibirnate.configuration.properties.LoggingProperties;
import com.svydovets.bibirnate.session.SessionFactory;
import com.svydovets.bibirnate.utils.ValidationUtils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DefaultSessionFactoryBuilderImpl extends AbstractSessionFactoryBuilderImpl
  implements SessionFactoryBuilder {

    private CacheProperties cacheProperties;
    private DatabaseProperties databaseProperties;
    private ConnectionPoolProperties connectionPoolProperties;
    private LoggingProperties sqlLoggingProperties;

    public DefaultSessionFactoryBuilderImpl withDatabaseConnection(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
        return this;
    }

    public DefaultSessionFactoryBuilderImpl withSecondLevelCache(CacheProperties cacheProperties1) {
        this.cacheProperties = cacheProperties1;
        return this;
    }

    public DefaultSessionFactoryBuilderImpl withConnectionPool(ConnectionPoolProperties connectionPoolProperties) {
        this.connectionPoolProperties = connectionPoolProperties;
        return this;
    }

    public DefaultSessionFactoryBuilderImpl withSqlQueriesLoggingEnabled(LoggingProperties sqlLoggingProperties) {
        this.sqlLoggingProperties = sqlLoggingProperties;
        return this;
    }

    @Override
    public SessionFactory build() {
        ValidationUtils.validateNotNullDatabaseFields(DatabaseProperties.class, databaseProperties);
        var configurationProperties = populateConfigurationPropertiesOrGetDefault();
        return buildSessionFactory(configurationProperties);
    }

    private ConfigurationProperties populateConfigurationPropertiesOrGetDefault() {
        var configurationProperties = new ConfigurationProperties();
        configurationProperties.setDatabase(databaseProperties);
        configurationProperties.setConnectionPool(checkNotNullOrGetDefault(connectionPoolProperties,
          ConnectionPoolProperties::new));
        configurationProperties.setSecondLevelCache(checkNotNullOrGetDefault(cacheProperties, CacheProperties::new));
        configurationProperties.setSqlLogging(checkNotNullOrGetDefault(sqlLoggingProperties, LoggingProperties::new));
        return configurationProperties;
    }

    private <T> T checkNotNullOrGetDefault(T instance, Supplier<T> defaultValue) {
        return Objects.nonNull(instance) ? instance : defaultValue.get();
    }
}
