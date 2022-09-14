package com.svydovets.bibirnate.utils;

import com.svydovets.bibirnate.configuration.properties.ConfigurationProperties;
import com.svydovets.bibirnate.configuration.properties.ConnectionPoolProperties;
import com.svydovets.bibirnate.configuration.properties.DatabaseProperties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HikariConfigUtilsTest {

    private static ConfigurationProperties configurationProperties = new ConfigurationProperties();

    private static void initConfigurationProperties() {
        var databaseProperties = new DatabaseProperties();
        databaseProperties.setUrl("testUrl");
        databaseProperties.setUser("testUser");
        databaseProperties.setPassword("testPassword");
        databaseProperties.setDriverName("org.postgresql.Driver");
        configurationProperties.setDatabase(databaseProperties);

        var connectionPoolProperties = new ConnectionPoolProperties();
        connectionPoolProperties.setCatalog("testCatalog");
        connectionPoolProperties.setSchema("testSchema");
        connectionPoolProperties.setPoolName("testPoolName");
        connectionPoolProperties.setDataSourceClassName("dataSourceClassName");
        connectionPoolProperties.setDataSourceJndiName("dataSourceJndiClass");
        connectionPoolProperties.setConnectionInitSql("connectionInitSql");
        connectionPoolProperties.setConnectionTestQuery("connectionTestQuery");
        connectionPoolProperties.setTransactionIsolationName("transactionIsolationName");
        connectionPoolProperties.setAutoCommit(true);
        connectionPoolProperties.setReadOnly(false);
        connectionPoolProperties.setIsolateInternalQueries(true);
        connectionPoolProperties.setRegisterMbeans(false);
        connectionPoolProperties.setAllowPoolSuspension(true);
        connectionPoolProperties.setMaxPoolSize(100);
        connectionPoolProperties.setMinIdle(2);
        connectionPoolProperties.setConnectionTimeout(250);
        connectionPoolProperties.setValidationTimeout(5000);
        connectionPoolProperties.setIdleTimeout(5);
        connectionPoolProperties.setLeakDetectionThreshold(0);
        connectionPoolProperties.setMaxLifetime(7);
        connectionPoolProperties.setInitializationFailTimeout(8);
        connectionPoolProperties.setKeepaliveTime(0);
        configurationProperties.setConnectionPool(connectionPoolProperties);
    }

    @Test
    void testCreateHikariConfigFromConfigurationProperties() {
        initConfigurationProperties();
        var expectedDatabaseProperties = configurationProperties.getDatabase();
        var expectedConnectionPoolProperties = configurationProperties.getConnectionPool();

        var hikariConfig = HikariConfigUtils.createHikariConfig(configurationProperties);
        assertEquals(expectedDatabaseProperties.getUrl(), hikariConfig.getJdbcUrl());
        assertEquals(expectedDatabaseProperties.getUser(), hikariConfig.getUsername());
        assertEquals(expectedDatabaseProperties.getPassword(), hikariConfig.getPassword());
        assertEquals(expectedDatabaseProperties.getDriverName(), hikariConfig.getDriverClassName());
        assertEquals(expectedConnectionPoolProperties.getCatalog(), hikariConfig.getCatalog());
        assertEquals(expectedConnectionPoolProperties.getSchema(), hikariConfig.getSchema());
        assertEquals(expectedConnectionPoolProperties.getPoolName(), hikariConfig.getPoolName());
        assertEquals(expectedConnectionPoolProperties.getDataSourceClassName(), hikariConfig.getDataSourceClassName());
        assertEquals(expectedConnectionPoolProperties.getDataSourceJndiName(), hikariConfig.getDataSourceJNDI());
        assertEquals(expectedConnectionPoolProperties.getConnectionInitSql(), hikariConfig.getConnectionInitSql());
        assertEquals(expectedConnectionPoolProperties.getConnectionTestQuery(), hikariConfig.getConnectionTestQuery());
        assertEquals(expectedConnectionPoolProperties.getTransactionIsolationName(), hikariConfig.getTransactionIsolation());
        assertEquals(expectedConnectionPoolProperties.isAutoCommit(), hikariConfig.isAutoCommit());
        assertEquals(expectedConnectionPoolProperties.isReadOnly(), hikariConfig.isReadOnly());
        assertEquals(expectedConnectionPoolProperties.isIsolateInternalQueries(), hikariConfig.isIsolateInternalQueries());
        assertEquals(expectedConnectionPoolProperties.isRegisterMbeans(), hikariConfig.isRegisterMbeans());
        assertEquals(expectedConnectionPoolProperties.isAllowPoolSuspension(), hikariConfig.isAllowPoolSuspension());
        assertEquals(expectedConnectionPoolProperties.getMaxPoolSize(), hikariConfig.getMaximumPoolSize());
        assertEquals(expectedConnectionPoolProperties.getMinIdle(), hikariConfig.getMinimumIdle());
        assertEquals(expectedConnectionPoolProperties.getConnectionTimeout(), hikariConfig.getConnectionTimeout());
        assertEquals(expectedConnectionPoolProperties.getValidationTimeout(), hikariConfig.getValidationTimeout());
        assertEquals(expectedConnectionPoolProperties.getIdleTimeout(), hikariConfig.getIdleTimeout());
        assertEquals(expectedConnectionPoolProperties.getLeakDetectionThreshold(), hikariConfig.getLeakDetectionThreshold());
        assertEquals(expectedConnectionPoolProperties.getMaxLifetime(), hikariConfig.getMaxLifetime());
        assertEquals(expectedConnectionPoolProperties.getInitializationFailTimeout(), hikariConfig.getInitializationFailTimeout());
        assertEquals(expectedConnectionPoolProperties.getKeepaliveTime(), hikariConfig.getKeepaliveTime());
    }
}
