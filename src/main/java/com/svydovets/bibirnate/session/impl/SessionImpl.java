package com.svydovets.bibirnate.session.impl;

import static com.svydovets.bibirnate.session.impl.JdbcEntityDaoFactory.createJdbcEntityDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.cache.CacheUtils;
import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.session.Session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.trace("Finding {} by id {}", type.getSimpleName(), id);

        checkIfSessionClosed();
        Optional<T> result = CacheUtils.extract(cacheContainer, type, id);

        if (result.isEmpty()) {
            log.trace("Entity was not found in cache, making request to DB");
            result = jdbcEntityDao.findById(id, type);
            CacheUtils.put(cacheContainer, type, id, result);
        } else {
            log.trace("Entity was found in cache");
        }

        return result.orElse(null);
    }

    @Override
    public void remove(Object entity) {
        log.trace("Removing {} by id", entity.getClass().getSimpleName());
        checkIfSessionClosed();
        jdbcEntityDao.remove(entity);
        CacheUtils.invalidate(cacheContainer, entity);
    }

    @Override
    public void close() {
        log.trace("Closing session");
        if (!closed) {
            try {
                connection.close();
                closed = true;
            } catch (SQLException ex) {
                log.trace("Cannot close session.");
                throw new JdbcException("Cannot close session. The purpose is " + ex.getMessage(), ex);
            }
        }
        log.trace("Session closed successfully");
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
