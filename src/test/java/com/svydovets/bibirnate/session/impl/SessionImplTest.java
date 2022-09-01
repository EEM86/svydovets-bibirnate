package com.svydovets.bibirnate.session.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.entities.EntityPrimitives;

import lombok.SneakyThrows;

class SessionImplTest {

    @Test
    @SneakyThrows
    void session_shouldCallJdbcEntryDaoFind() {
        try (var factory = mockStatic(JdbcEntityDaoFactory.class)) {
            var jdbcEntityDao = mock(JdbcEntityDao.class);
            factory.when(() -> JdbcEntityDaoFactory.createJdbcEntityDao(any())).thenReturn(jdbcEntityDao);
            var session = new SessionImpl(new PGSimpleDataSource(), new Cache(40_000));
            session.findById(12L, EntityPrimitives.class);
            verify(jdbcEntityDao).findById(12L, EntityPrimitives.class);
        }
    }

}
