package com.svydovets.bibirnate.jdbc;

import com.svydovets.bibirnate.SessionTestUtil;
import com.svydovets.bibirnate.jdbc.impl.PostgresJdbcEntityDao;
import com.svydovets.bibirnate.logs.SqlLogger;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class JdbcEntityDaoFactoryTest {

    JdbcEntityDaoFactory factory;

    @Test
    void testCreateJdbcEntityDao_shouldReturnPostgresJdbcEntityDao_whenPostgresConfigured() throws SQLException {
        var connection = mock(Connection.class);
        SessionTestUtil.mockConnectionMetadata(connection);
        final JdbcEntityDao jdbcEntityDao = factory.createJdbcEntityDao(connection, new SqlLogger(false));

        assertTrue(jdbcEntityDao instanceof PostgresJdbcEntityDao);
    }

    @Test()
    void testCreateJdbcEntityDao_shouldThrowException_whenUnknownDriverConfigured() throws SQLException {
        var connection = mock(Connection.class);
        SessionTestUtil.mockConnectionMetadata(connection, "jdbc:mock://localhost:29211");
        var exception = assertThrows(SQLException.class,
            () -> factory.createJdbcEntityDao(connection, new SqlLogger(false)));

        assertEquals("No suitable driver", exception.getMessage());
    }

}