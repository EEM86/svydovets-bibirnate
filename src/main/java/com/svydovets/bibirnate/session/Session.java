package com.svydovets.bibirnate.session;

/**
 * This is main interface to work with Bibirnate. It allows to read operations for mapped classes.
 */
public interface Session {
    /**
     * Find entity with provided entity type and identifier.
     *
     * @param id of entity
     * @param type mapped class with {@link com.svydovets.bibirnate.annotation.Entity}
     * @param <T>  type of entity
     * @return found entity or null if no such entity was found
     */
    <T> T findById(Object id, Class<T> type);
}
