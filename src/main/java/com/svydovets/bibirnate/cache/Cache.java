package com.svydovets.bibirnate.cache;

import com.svydovets.bibirnate.util.EntityKey;

import java.util.Optional;

/**
 * The cache provides opportunities to save an entity and in case when application need it id kan be provided by a key,
 * as a key the Bibernate uses for this purpose {@link EntityKey}. Also, {@link Cache} provides some methods for
 * working with this cache.
 */
public interface Cache {

    <T> void put(EntityKey<T> entityKey, T entity);

    <T> Optional<T> get(Object id, Class<T> entityType);

    <T> void delete(EntityKey<T> entityKey);

    void clear();

    boolean isEnabled();

    void enable();

    void disable();
}
