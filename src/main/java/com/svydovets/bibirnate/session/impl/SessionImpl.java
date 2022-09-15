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
import com.svydovets.bibirnate.exceptions.PersistenceException;
import com.svydovets.bibirnate.exceptions.TransactionManagerException;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.logs.SqlLogger;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.query.Query;
import com.svydovets.bibirnate.session.query.TypedQuery;
import com.svydovets.bibirnate.session.transaction.TransactionManager;
import com.svydovets.bibirnate.session.transaction.TransactionManagerImpl;
import com.svydovets.bibirnate.utils.EntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Basic implementation of the {@link Session}.
 */
@Slf4j
public class SessionImpl implements Session {
    private final JdbcEntityDao jdbcEntityDao;
    private final CacheContainer cacheContainer;
    private final Connection connection;
    private final TransactionManager transactionManager;
    private final SqlLogger sqlLogger;
    private boolean closed;

    public SessionImpl(Connection connection, CacheContainer cacheContainer, SqlLogger sqlLogger) {
        this.jdbcEntityDao = createJdbcEntityDao(connection, sqlLogger);
        this.cacheContainer = cacheContainer;
        this.connection = connection;
        this.transactionManager = new TransactionManagerImpl(connection);
        this.sqlLogger = sqlLogger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T findById(Object id, Class<T> type) {
        log.trace("Finding {} by id {}", type.getSimpleName(), id);

        checkIfSessionClosed();
        Optional<T> result = CacheUtils.extract(cacheContainer, type, id);

        if (result.isEmpty()) {
            log.trace("Entity was not found in cache, making request to DB");
            result = jdbcEntityDao.findById(id, type);
            result.ifPresent(entity -> CacheUtils.put(cacheContainer, type, id, entity));
        } else {
            log.trace("Entity was found in cache");
        }

        return result.orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Object entity) {
        log.trace("Removing {} by id", entity.getClass().getSimpleName());
        checkIfSessionClosed();
        var id = EntityUtils.getEntityIdValue(entity);
        var result = CacheUtils.extract(cacheContainer, entity.getClass(), id);
//        if (result.isPresent()) {
            try {
                try {
                    transactionManager.begin();
                    jdbcEntityDao.remove(entity);
                    CacheUtils.invalidate(cacheContainer, entity);
                    transactionManager.commit();
                } catch (Exception ex) {
                    transactionManager.rollback();
                }
            } catch (TransactionManagerException ex) {
                throw new PersistenceException("Could not rollback transaction", ex);
            }
//        } else {
//            throw new PersistenceException(
//              String.format("Object %s can not be deleted as it is not present in Session", entity));
//        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isClosed() {
        return closed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Query createTypedQuery(String sql, Class<T> entityType) {
        log.trace("Creation TypedQuery with SQL [{}] and for the entity type [{}].", sql, entityType);
        if (StringUtils.isBlank(sql)) {
            throw new BibernateException("Passed SQL query cannot be null or blank. Please take a look at passed SQL "
              + "query for the Session#createTypedQuery");
        }
        var msg = "[entityType] cannot be null. Please provide a class of the entity for what you create a query.";
        Objects.requireNonNull(entityType, msg);

        return new TypedQuery(jdbcEntityDao, sql, entityType, cacheContainer, sqlLogger);
    }

    private void checkIfSessionClosed() {
        if (closed) {
            throw new JdbcException("Session is already closed");
        }
    }

}
