package com.svydovets.bibirnate.session.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.session.SessionFactory;

import lombok.SneakyThrows;

class SessionFactoryImplTest {

    @Test
    @SneakyThrows
    void openSessionShouldThrowJdbcExceptionIfDataSourceThrowsSqlException() {
        var datasource = mock(DataSource.class);
        when(datasource.getConnection()).thenThrow(SQLException.class);

        var sessionFactory = new SessionFactoryImpl(datasource);

        assertThrows(JdbcException.class, sessionFactory::openSession);
    }

    @Test
    void openSessionShouldNotReturnNull() {
        var datasource = mock(DataSource.class);
        SessionFactory sessionFactory = new SessionFactoryImpl(datasource);
        assertNotNull(sessionFactory.openSession());
    }
}
