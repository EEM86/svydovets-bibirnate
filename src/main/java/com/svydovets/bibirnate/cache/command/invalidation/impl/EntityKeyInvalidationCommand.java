package com.svydovets.bibirnate.cache.command.invalidation.impl;

import com.svydovets.bibirnate.cache.command.invalidation.InvalidationCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkOnIsAssignableTo;
import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkPassedParametersOnNull;
import static com.svydovets.bibirnate.cache.command.util.CommandUtil.removeAllCacheWithQueryKeyRelated;

/**
 * This is the realization of the {@link InvalidationCommand} that makes invalidation for caches related to passed
 * {@link Key}.
 */
public class EntityKeyInvalidationCommand implements InvalidationCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeInvalidate(Map<Key<?>, Object> cacheMap, Key<?> key) {
        // todo: add logger with info that invalidation caches process is started
        checkPassedParametersOnNull(cacheMap, key);

        EntityKeyParam<?> entityKeyParam = (EntityKeyParam<?>) checkOnIsAssignableTo(key.getKeyParam(),
                EntityKeyParam.class);
        Class<?> entityType = entityKeyParam.getEntityType();

        removeCacheByEntityKeyWithSameId(cacheMap, entityKeyParam, entityType);
        removeAllCacheWithQueryKeyRelated(cacheMap, entityType);
    }

    private void removeCacheByEntityKeyWithSameId(Map<Key<?>, Object> cacheMap, EntityKeyParam<?> entityKeyParam,
                                                  Class<?> entityType) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        cacheMap.keySet().stream()
                .filter(k -> k.getKeyParam().getClass().isAssignableFrom(EntityKeyParam.class))
                .filter(k -> k.getKeyParam().getEntityType().isAssignableFrom(entityType))
                .filter(k -> ((EntityKeyParam<?>) k.getKeyParam()).getId().equals(entityKeyParam.getId()))
                .forEach(cacheMap::remove);

        executorService.shutdown();
    }

}
