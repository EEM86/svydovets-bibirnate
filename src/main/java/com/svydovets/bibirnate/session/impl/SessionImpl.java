package com.svydovets.bibirnate.session.impl;

import java.util.Optional;
import javax.sql.DataSource;

import com.svydovets.bibirnate.session.Session;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionImpl implements Session {
    private final JdbcEntityDao jdbcEntityDao;

    public SessionImpl(DataSource dataSource) {
        this.jdbcEntityDao = new JdbcEntityDao(dataSource);
    }

    @Override
    public <T> Optional<T> findById(Object id, Class<T> type) {
        return jdbcEntityDao.findById(id, type);
    }
}
