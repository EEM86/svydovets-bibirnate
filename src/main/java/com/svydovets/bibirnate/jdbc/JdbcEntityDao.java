package com.svydovets.bibirnate.jdbc;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Interface to work with database.
 */
public interface JdbcEntityDao {

    /**
     * Get Optional of entity by id.
     *
     * @param id Object
     * @param type of entity
     * @param <T> entity
     * @return Optional of entity or empty Optional
     */
    <T> Optional<T> findById(Object id, Class<T> type);

    /**
     * Get Optional of entity by field (column name).
     *
     * @param field column name
     * @param value condition used in 'where = ?'
     * @param type of entity
     * @param <T> entity
     * @return Optional of entity or empty Optional
     */
    <T> Optional<T> findBy(Field field, Object value, Class<T> type);

    /**
     * Delete entity from database.
     *
     * @param entity to remove
     */
    void remove(Object entity);

}
