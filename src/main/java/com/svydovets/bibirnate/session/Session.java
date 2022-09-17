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

    /**
     * Update provided entity in the DB.
     *
     * @param entity - entity that should be updated
     */
    void update(Object entity);

    /**
     * Delete existing project from Database.
     *
     * @param entity - entity that should be removed
     */
    void remove(Object entity);

    /**
     * Save entity to the database.
     *
     * @param entity to save
     */
    void persist(Object entity);

    /**
     * Checks if session is already closed.
     *
     * @return is the session closed
     */
    boolean isClosed();

    /**
     * Provides {@link TransactionManager} for control over transaction (transaction.begin() || transaction.commit() ||
     * transaction.rollback()).
     *
     * @return instance of the {@link TransactionManager}
     */
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

    /**
     * Do flush all changes to Database.
     */
    void flush();
}
