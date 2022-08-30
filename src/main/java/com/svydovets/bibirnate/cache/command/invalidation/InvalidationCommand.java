package com.svydovets.bibirnate.cache.command.invalidation;

import java.util.Map;

import com.svydovets.bibirnate.cache.key.Key;

/**
 * This interface is start contract for 'cache invalidation' commands.
 */
public interface InvalidationCommand {

    /**
     * By this method can be made invalidation of cache by the {@link Key} and related to this {@link Key}.
     *
     * @param cacheMap {@link Map} with cache
     * @param key      {@link Key}
     */
    void executeInvalidate(Map<Key<?>, Object> cacheMap, Key<?> key);

}
