package com.svydovets.bibirnate.session.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.postgresql.ds.PGSimpleDataSource;

import com.svydovets.bibirnate.entities.EntityPrimitives;

class SessionImplTest {

    @Test
    void session_shouldCallJdbcEntryDaoFind() {
        try (var utilities = Mockito.mockStatic(JdbcEntityDaoFactory.class)) {
            var jdbcEntityDao = mock(JdbcEntityDao.class);
            utilities.when(() -> JdbcEntityDaoFactory.createJdbcEntityDao(any())).thenReturn(jdbcEntityDao);

            var session = new SessionImpl(new PGSimpleDataSource());
            session.findById(12L, EntityPrimitives.class);
            verify(jdbcEntityDao).findById(12L, EntityPrimitives.class);
        }
    }

}