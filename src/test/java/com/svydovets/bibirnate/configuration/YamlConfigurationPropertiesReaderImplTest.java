package com.svydovets.bibirnate.configuration;

import com.svydovets.bibirnate.configuration.properties.ConfigurationProperties;
import com.svydovets.bibirnate.exceptions.PropertiesFileInvalidException;
import com.svydovets.bibirnate.exceptions.PropertiesFileMissingException;
import com.svydovets.bibirnate.exceptions.PropertiesFileValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YamlConfigurationPropertiesReaderImplTest {

    private final YamlConfigurationPropertiesReaderImpl reader = new YamlConfigurationPropertiesReaderImpl();

    private ConfigurationProperties configurationProperties;

    @Test
    void testReadYamlDatabasePropertiesSuccess() {
        configurationProperties = reader.readProperties("persistence.yaml");
        var databaseProperties = configurationProperties.getDatabase();
        assertEquals("url", databaseProperties.getUrl());
        assertEquals("user", databaseProperties.getUser());
        assertEquals("pass", databaseProperties.getPassword());
        assertEquals("databaseDriver", databaseProperties.getDriverName());
    }

    @Test
    void testReadYamlConnectionPoolPropertiesSuccess() {
        configurationProperties = reader.readProperties("persistence.yaml");
        var connectionPoolProperties = configurationProperties.getConnectionPool();
        assertEquals("catalog", connectionPoolProperties.getCatalog());
        assertEquals("schema", connectionPoolProperties.getSchema());
        assertEquals("poolName", connectionPoolProperties.getPoolName());
        assertEquals("dataSourceClassName", connectionPoolProperties.getDataSourceClassName());
        assertEquals("dataSourceJndiClass", connectionPoolProperties.getDataSourceJndiName());
        assertEquals("connectionInitSql", connectionPoolProperties.getConnectionInitSql());
        assertEquals("connectionTestQuery", connectionPoolProperties.getConnectionTestQuery());
        assertEquals("transactionIsolationName", connectionPoolProperties.getTransactionIsolationName());
        assertEquals("exceptionOverrideClassName", connectionPoolProperties.getExceptionOverrideClassName());
        assertTrue(connectionPoolProperties.isAutoCommit());
        assertFalse(connectionPoolProperties.isReadOnly());
        assertTrue(connectionPoolProperties.isIsolateInternalQueries());
        assertFalse(connectionPoolProperties.isRegisterMbeans());
        assertTrue(connectionPoolProperties.isAllowPoolSuspension());
        assertEquals(1, connectionPoolProperties.getMaxPoolSize());
        assertEquals(2, connectionPoolProperties.getMinIdle());
        assertEquals(3, connectionPoolProperties.getConnectionTimeout());
        assertEquals(4, connectionPoolProperties.getValidationTimeout());
        assertEquals(5, connectionPoolProperties.getIdleTimeout());
        assertEquals(6, connectionPoolProperties.getLeakDetectionThreshold());
        assertEquals(7, connectionPoolProperties.getMaxLifetime());
        assertEquals(8, connectionPoolProperties.getInitializationFailTimeout());
        assertEquals(9, connectionPoolProperties.getKeepaliveTime());
    }

    @Test
    void testReadYamlCachePropertiesSuccess() {
        configurationProperties = reader.readProperties("persistence.yaml");
        assertTrue(configurationProperties.getSecondLevelCache().isEnabled());
    }

    @Test
    void testReadYamlDatabasePropertiesThrowsPropertiesFileMissingException() {
        Assertions.assertThrows(PropertiesFileMissingException.class, () -> configurationProperties = reader.readProperties("non-existing-file.yaml"));
    }

    @Test
    void testReadYamlDatabasePropertiesThrowsInvalidFileException() {
        Assertions.assertThrows(PropertiesFileInvalidException.class, () -> configurationProperties = reader.readProperties("persistence-not-valid.yaml"));
    }

    @Test
    void testReadYamlDatabasePropertiesThrowsValidationException() {
        Assertions.assertThrows(PropertiesFileValidationException.class, () -> configurationProperties = reader.readProperties("persistence-missing-property.yaml"));
    }
}
