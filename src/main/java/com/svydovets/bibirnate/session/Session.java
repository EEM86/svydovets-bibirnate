package com.svydovets.bibirnate.session;

import java.io.Closeable;

import com.svydovets.bibirnate.annotation.Entity;

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

    void remove(Object entity);

    /**
     * Checks if session is already closed.
     *
     * @return is the session closed
     */
    boolean isClosed();

}
