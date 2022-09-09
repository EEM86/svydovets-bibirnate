package com.svydovets.bibirnate.session.impl;

import static com.svydovets.bibirnate.session.impl.JdbcEntityDaoFactory.createJdbcEntityDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.cache.CacheUtils;
import com.svydovets.bibirnate.session.Session;

public class SessionImpl implements Session {
    private final JdbcEntityDao jdbcEntityDao;
    private final CacheContainer cacheContainer;

    public SessionImpl(DataSource dataSource, CacheContainer cacheContainer) {
        this.jdbcEntityDao = createJdbcEntityDao(dataSource);
        this.cacheContainer = cacheContainer;
    private final Connection connection;

    private boolean closed;

    public SessionImpl(Connection connection) {
        this.jdbcEntityDao = createJdbcEntityDao(connection);
        this.connection = connection;
    }

    @Override
    public <T> Optional<T> findById(Object id, Class<T> type) {
        if (closed) {
            throw new JdbcException("Session is already closed");
        }
        return jdbcEntityDao.findById(id, type);
    public <T> T findById(Object id, Class<T> type) {
        Optional<T> result = CacheUtils.extract(cacheContainer, type, id);

        if (result.isEmpty()) {
            result = jdbcEntityDao.findById(id, type);
            CacheUtils.put(cacheContainer, type, id, result);
        }

        return result.orElse(null);
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new JdbcException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
