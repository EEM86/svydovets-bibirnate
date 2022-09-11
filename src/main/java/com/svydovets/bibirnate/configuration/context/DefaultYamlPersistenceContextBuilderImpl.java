package com.svydovets.bibirnate.configuration.context;

import javax.sql.DataSource;

import org.apache.logging.log4j.spi.LoggerContext;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.configuration.ConfigurationProperties;
import com.svydovets.bibirnate.configuration.YamlConfigurationPropertiesReaderImpl;
import com.svydovets.bibirnate.session.SessionFactory;
import com.svydovets.bibirnate.session.impl.SessionFactoryImpl;
import com.svydovets.bibirnate.utils.HikariConfigUtils;
import com.zaxxer.hikari.HikariDataSource;


public class DefaultYamlPersistenceContextBuilderImpl implements PersistenceContextBuilder {

    private String fileName;

    @Override
    public PersistenceContext build() {
        var configurationProperties = new YamlConfigurationPropertiesReaderImpl().readProperties(fileName);
        var datasource = initializeDataSource(configurationProperties);
        var cacheContainer = initializeCacheContainer(configurationProperties);
        var sessionFactory = initializeSessionFactory(datasource, cacheContainer);
        var loggerContext = initializeLoggerContext();
        return new DefaultYamlPersistenceContextImpl(sessionFactory, loggerContext);
    }

    public PersistenceContextBuilder fromFile(String fileName) {
        this.fileName = fileName;
        return this;
    }

    private SessionFactory initializeSessionFactory(DataSource datasource, CacheContainer cacheContainer) {
        return new SessionFactoryImpl(datasource, cacheContainer);
    }

    private CacheContainer initializeCacheContainer(ConfigurationProperties configurationProperties) {
        return configurationProperties.getCache().isSecondLevelCacheEnabled() ? new CacheContainer(new Cache(), true)
                : new CacheContainer();
    }

    private LoggerContext initializeLoggerContext() {
        //TODO: configure logger to log SQL queries based on persistence.yaml configuration
        return new org.apache.logging.log4j.core.LoggerContext("GLOBAL");
    }

    protected DataSource initializeDataSource(ConfigurationProperties configurationProperties) {
        var hikariConfig = HikariConfigUtils.createHikariConfig(configurationProperties);
        return new HikariDataSource(hikariConfig);
    }
}
