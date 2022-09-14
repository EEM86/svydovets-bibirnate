package com.svydovets.bibirnate.session.impl;

import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.session.SessionFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import static com.svydovets.bibirnate.SessionTestUtil.mockConnectionMetadata;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionFactoryImplTest {

    @Test
    @SneakyThrows
    void openSessionShouldThrowJdbcExceptionIfDataSourceThrowsSqlException() {
        var datasource = mock(DataSource.class);
        when(datasource.getConnection()).thenThrow(SQLException.class);

        var sessionFactory = new SessionFactoryImpl(datasource, false, 0, false);

        assertThrows(JdbcException.class, sessionFactory::openSession);
    }

    @Test
    void openSessionShouldNotReturnNull() throws SQLException {
        var datasource = mock(DataSource.class);
        var connection = mock(Connection.class);

        when(datasource.getConnection()).thenReturn(connection);
        mockConnectionMetadata(connection);

        SessionFactory sessionFactory = new SessionFactoryImpl(datasource, false, 0, false);
        assertNotNull(sessionFactory.openSession());
    }
}
