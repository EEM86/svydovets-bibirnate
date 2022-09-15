package com.svydovets.bibirnate.session.impl;

import static com.svydovets.bibirnate.jdbc.JdbcEntityDaoFactory.createJdbcEntityDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.cache.CacheUtils;
import com.svydovets.bibirnate.exceptions.BibernateException;
import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.query.Query;
import com.svydovets.bibirnate.session.query.TypedQuery;
import com.svydovets.bibirnate.session.transaction.TransactionManager;
import com.svydovets.bibirnate.session.transaction.TransactionManagerImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionImpl implements Session {
    private final JdbcEntityDao jdbcEntityDao;
    private final CacheContainer cacheContainer;
    private final Connection connection;
    private final TransactionManager transactionManager;
    private boolean closed;

    public SessionImpl(Connection connection, CacheContainer cacheContainer) {
        this.jdbcEntityDao = createJdbcEntityDao(connection);
        this.cacheContainer = cacheContainer;
        this.connection = connection;
        this.transactionManager = new TransactionManagerImpl(connection);
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

    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    public <T> Query createTypedQuery(String sql, Class<T> entityType) {
        log.trace("Creation TypedQuery with SQL [{}] and for the entity type [{}].", sql, entityType);
        if (StringUtils.isBlank(sql)) {
            throw new BibernateException("Passed SQL query cannot be null or blank. Please take a look at passed SQL "
              + "query for the Session#createTypedQuery");
        }
        var msg = "[entityType] cannot be null. Please provide a class of the entity for what you create a query.";
        Objects.requireNonNull(entityType, msg);

        return new TypedQuery(jdbcEntityDao, sql, entityType, cacheContainer);
    }

    private void checkIfSessionClosed() {
        if (closed) {
            throw new JdbcException("Session is already closed");
        }
    }

}
