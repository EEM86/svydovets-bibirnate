package com.svydovets.bibirnate.session.impl;

import com.svydovets.bibirnate.session.Session;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.util.Optional;

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
