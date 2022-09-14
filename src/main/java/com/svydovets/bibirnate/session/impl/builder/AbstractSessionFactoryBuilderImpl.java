package com.svydovets.bibirnate.session.impl.builder;

import com.svydovets.bibirnate.configuration.properties.ConfigurationProperties;
import com.svydovets.bibirnate.session.SessionFactory;
import com.svydovets.bibirnate.session.impl.SessionFactoryImpl;
import com.svydovets.bibirnate.utils.HikariConfigUtils;
import com.zaxxer.hikari.HikariDataSource;

public abstract class AbstractSessionFactoryBuilderImpl implements SessionFactoryBuilder {

    protected SessionFactory buildSessionFactory(ConfigurationProperties configurationProperties) {
        var hikariConfig = HikariConfigUtils.createHikariConfig(configurationProperties);
        return new SessionFactoryImpl(new HikariDataSource(hikariConfig),
          configurationProperties.getSecondLevelCache().isEnabled(),
          configurationProperties.getSecondLevelCache().getSize(),
          configurationProperties.getSqlLogging().isEnabled());
    }
}
