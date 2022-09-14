package com.svydovets.bibirnate.session.impl;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.entities.EntityPrimitives;
import com.svydovets.bibirnate.entities.PersonSimpleEntity;
import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.jdbc.JdbcEntityDaoFactory;
import com.svydovets.bibirnate.jdbc.impl.BaseJdbcEntityDao;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.transaction.TransactionManagerImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static com.svydovets.bibirnate.SessionTestUtil.mockConnectionMetadata;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

class SessionImplTest {

    @Test
    @SneakyThrows
    void session_shouldCallJdbcEntryDaoFind() {
        try (var factory = mockStatic(JdbcEntityDaoFactory.class)) {
            var jdbcEntityDao = mock(BaseJdbcEntityDao.class);
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
        mockConnectionMetadata(connection);
        Session session = new SessionImpl(connection, cacheContainer);
        session.close();
        Assertions.assertThrows(JdbcException.class, () -> session.findById(12L, EntityPrimitives.class));
    }

    @Test
    @SneakyThrows
    void close_shouldCloseSession() {
        var connection = mock(Connection.class);
        var cacheContainer = mock(CacheContainer.class);

        mockConnectionMetadata(connection);

        var session = new SessionImpl(connection, cacheContainer);
        session.close();

        assertTrue(session.isClosed());
    }

    @Test
    void remove() {
        try (var factory = mockStatic(JdbcEntityDaoFactory.class)) {
            var jdbcEntityDao = mock(JdbcEntityDao.class);
            factory.when(() -> JdbcEntityDaoFactory.createJdbcEntityDao(any())).thenReturn(jdbcEntityDao);
            var session = new SessionImpl(any(), new CacheContainer(new Cache(40_000), false));
            session.remove(new PersonSimpleEntity());
            verify(jdbcEntityDao).remove(any(PersonSimpleEntity.class));
        }
    }

    @Test
    void getTransaction_shouldGetTransactionManagerImpl() {
        try (var factory = mockStatic(JdbcEntityDaoFactory.class)) {
            var jdbcEntityDao = mock(JdbcEntityDao.class);
            factory.when(() -> JdbcEntityDaoFactory.createJdbcEntityDao(any())).thenReturn(jdbcEntityDao);
            var session = new SessionImpl(any(), new CacheContainer(new Cache(40_000), false));
            var transactionManager = session.getTransactionManager();

            assertNotNull(transactionManager);
            assertEquals(TransactionManagerImpl.class, transactionManager.getClass());
        }
    }
}
