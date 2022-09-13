package com.svydovets.bibirnate.session.impl;

import static com.svydovets.bibirnate.SessionTestUtil.mockConnectionMetadata;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.entities.EntityPrimitives;
import com.svydovets.bibirnate.entities.PersonSimpleEntity;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.exceptions.BibernateException;
import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.jdbc.JdbcEntityDaoFactory;
import com.svydovets.bibirnate.jdbc.impl.BaseJdbcEntityDao;
import com.svydovets.bibirnate.session.Session;

import lombok.SneakyThrows;

class SessionImplTest {

    private Session session;

    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = mock(Connection.class);
        CacheContainer cacheContainer = mock(CacheContainer.class);
        mockConnectionMetadata(connection);
        session = new SessionImpl(connection, cacheContainer);
    }

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

    @ParameterizedTest
    @MethodSource("createTypedQuery_provideParamsForBibernateException")
    void createTypedQuery_throwsBibernateException(String query, Class<?> entityType) {
        assertThrows(BibernateException.class, () -> session.createTypedQuery(query, entityType));
    }

    @Test
    void createTypedQuery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> session.createTypedQuery("select", null));
    }

    @Test
    void createTypedQuery_providesNewQuery() {
        assertNotNull(session.createTypedQuery("select", BiberEntity.class));
    }

    private static Stream<Arguments> createTypedQuery_provideParamsForBibernateException() {
        return Stream.of(Arguments.of(null, BiberEntity.class),
          Arguments.of("", BiberEntity.class),
          Arguments.of(" ", BiberEntity.class),
          Arguments.of("    ", BiberEntity.class)
        );
    }

}
