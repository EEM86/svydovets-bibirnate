package com.svydovets.bibirnate.session.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.sql.Connection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.entities.EntityPrimitives;
import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.session.Session;

import lombok.SneakyThrows;

class SessionImplTest {

    @Test
    @SneakyThrows
    void session_shouldCallJdbcEntryDaoFind() {
        try (var factory = mockStatic(JdbcEntityDaoFactory.class)) {
            var jdbcEntityDao = mock(JdbcEntityDao.class);
            factory.when(() -> JdbcEntityDaoFactory.createJdbcEntityDao(any())).thenReturn(jdbcEntityDao);
            var session = new SessionImpl(any(), new CacheContainer(new Cache(40_000), false));
            session.findById(12L, EntityPrimitives.class);
            verify(jdbcEntityDao).findById(12L, EntityPrimitives.class);
        }
    }

    @Test
    @SneakyThrows
    void findById_shouldThrowJdbcExceptionIfSessionAlreadyClosed() {
        Connection connection = mock(Connection.class);
        CacheContainer cacheContainer = mock(CacheContainer.class);
        Session session = new SessionImpl(connection, cacheContainer);
        session.close();
        Assertions.assertThrows(JdbcException.class, () -> session.findById(12L, EntityPrimitives.class));
    }

    @Test
    @SneakyThrows
    void close_shouldCloseSession() {
        Connection connection = mock(Connection.class);
        CacheContainer cacheContainer = mock(CacheContainer.class);
        Session session = new SessionImpl(connection, cacheContainer);
        session.close();

        assertTrue(session.isClosed());
    }

}
