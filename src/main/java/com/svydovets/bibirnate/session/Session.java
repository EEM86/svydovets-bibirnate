package com.svydovets.bibirnate.session;

import java.io.Closeable;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.session.query.Query;
import com.svydovets.bibirnate.session.query.TypedQuery;
import com.svydovets.bibirnate.session.transaction.TransactionManager;

/**
 * This is main interface to work with Bibirnate. It allows to read operations for mapped classes.
 */
public interface Session extends Closeable {
    /**
     * Find entity with provided entity type and identifier.
     *
     * @param id   of entity
     * @param type mapped class with {@link Entity}
     * @param <T>  type of entity
     * @return found entity wrapped in optional
     */
    <T> T findById(Object id, Class<T> type);

    void update(Object entity);

    void remove(Object entity);

    /**
     * Checks if session is already closed.
     *
     * @return is the session closed
     */
    boolean isClosed();

    TransactionManager getTransactionManager();

    /**
     * Creates instance {@link TypedQuery}.
     *
     * @param sql query
     * @param entityType class of entity
     * @return instance of {@link Query}
     * @param <T> entityType
     */
    <T> Query createTypedQuery(String sql, Class<T> entityType);

    void flush();
}
