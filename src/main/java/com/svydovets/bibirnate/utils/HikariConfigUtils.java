package com.svydovets.bibirnate.utils;

import com.svydovets.bibirnate.configuration.properties.ConfigurationProperties;
import com.zaxxer.hikari.HikariConfig;

/**
 * Utility class for create {@link HikariConfig} from own configuration.
 */
public class HikariConfigUtils {

    private HikariConfigUtils() {
    }

    public static HikariConfig createHikariConfig(ConfigurationProperties properties) {
        var hikariConfig = new HikariConfig();

        var databaseProperties = properties.getDatabase();
        hikariConfig.setJdbcUrl(databaseProperties.getUrl());
        hikariConfig.setUsername(databaseProperties.getUser());
        hikariConfig.setPassword(databaseProperties.getPassword());
        hikariConfig.setDriverClassName(databaseProperties.getDriverName());

        var connectionPoolProperties = properties.getConnectionPool();
        hikariConfig.setCatalog(connectionPoolProperties.getCatalog());
        hikariConfig.setConnectionTimeout(connectionPoolProperties.getConnectionTimeout());
        hikariConfig.setIdleTimeout(connectionPoolProperties.getIdleTimeout());
        hikariConfig.setMaxLifetime(connectionPoolProperties.getMaxLifetime());
        hikariConfig.setMaximumPoolSize(connectionPoolProperties.getMaxPoolSize());
        hikariConfig.setMinimumIdle(connectionPoolProperties.getMinIdle());
        hikariConfig.setInitializationFailTimeout(connectionPoolProperties.getInitializationFailTimeout());
        hikariConfig.setConnectionInitSql(connectionPoolProperties.getConnectionInitSql());
        hikariConfig.setConnectionTestQuery(connectionPoolProperties.getConnectionTestQuery());
        hikariConfig.setDataSourceClassName(connectionPoolProperties.getDataSourceClassName());
        hikariConfig.setDataSourceJNDI(connectionPoolProperties.getDataSourceJndiName());
        hikariConfig.setPoolName(connectionPoolProperties.getPoolName());
        hikariConfig.setSchema(connectionPoolProperties.getSchema());
        hikariConfig.setTransactionIsolation(connectionPoolProperties.getTransactionIsolationName());
        hikariConfig.setAutoCommit(connectionPoolProperties.isAutoCommit());
        hikariConfig.setReadOnly(connectionPoolProperties.isReadOnly());
        hikariConfig.setIsolateInternalQueries(connectionPoolProperties.isIsolateInternalQueries());
        hikariConfig.setRegisterMbeans(connectionPoolProperties.isRegisterMbeans());
        hikariConfig.setAllowPoolSuspension(connectionPoolProperties.isAllowPoolSuspension());

        return hikariConfig;
    }
}
