package com.svydovets.bibirnate.session.impl;

import static com.svydovets.bibirnate.jdbc.JdbcEntityDaoFactory.createJdbcEntityDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.cache.CacheUtils;
import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.session.Session;

public class SessionImpl implements Session {
    private final JdbcEntityDao jdbcEntityDao;
    private final CacheContainer cacheContainer;
    private final Connection connection;
    private boolean closed;

    public SessionImpl(Connection connection, CacheContainer cacheContainer) {
        this.jdbcEntityDao = createJdbcEntityDao(connection);
        this.cacheContainer = cacheContainer;
        this.connection = connection;
    }

    @Override
    public <T> T findById(Object id, Class<T> type) {
        checkIfSessionClosed();
        Optional<T> result = CacheUtils.extract(cacheContainer, type, id);

        if (result.isEmpty()) {
            result = jdbcEntityDao.findById(id, type);
            CacheUtils.put(cacheContainer, type, id, result);
        }

        return result.orElse(null);
    }

    @Override
    public void remove(Object entity) {
        checkIfSessionClosed();
        jdbcEntityDao.remove(entity);
        CacheUtils.invalidate(cacheContainer, entity);
    }

    @Override
    public void close() {
        if (!closed) {
            try {
                connection.close();
                closed = true;
            } catch (SQLException ex) {
                throw new JdbcException("Cannot close session. The purpose is " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    private void checkIfSessionClosed() {
        if (closed) {
            throw new JdbcException("Session is already closed");
        }
    }

}
