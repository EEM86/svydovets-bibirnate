package com.svydovets.bibirnate.cache.command.invalidation.impl;

import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkOnIsAssignableTo;
import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkPassedParametersOnNull;
import static com.svydovets.bibirnate.cache.command.util.CommandUtil.removeAllCacheWithQueryKeyRelated;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.svydovets.bibirnate.cache.command.invalidation.InvalidationCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;

import lombok.extern.slf4j.Slf4j;

/**
 * This is the realization of the {@link InvalidationCommand} that makes invalidation for caches related to passed
 * {@link Key}.
 */
@Slf4j
public class EntityKeyInvalidationCommand implements InvalidationCommand {

    public EntityKeyInvalidationCommand() {
        log.trace("New EntityKeyInvalidationCommand object is created.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeInvalidate(Map<Key<?>, Object> cacheMap, Key<?> key) {
        log.trace("Cache invalidation started for EntityCache");
        checkPassedParametersOnNull(cacheMap, key);

        var entityKeyParam = (EntityKeyParam<?>) checkOnIsAssignableTo(key.getKeyParam(), EntityKeyParam.class);
        var entityType = entityKeyParam.getEntityType();

        removeCacheByEntityKeyWithSameId(cacheMap, entityKeyParam, entityType);
        removeAllCacheWithQueryKeyRelated(cacheMap, entityType);
    }

    private void removeCacheByEntityKeyWithSameId(Map<Key<?>, Object> cacheMap, EntityKeyParam<?> entityKeyParam,
                                                  Class<?> entityType) {
        log.trace("Starting invalidation for EntityCache in new thread...");
        var executorService = Executors.newSingleThreadExecutor();
        cacheMap.keySet().stream()
          .filter(k -> k.getKeyParam().getClass().isAssignableFrom(EntityKeyParam.class))
          .filter(k -> k.getKeyParam().getEntityType().isAssignableFrom(entityType))
          .filter(k -> ((EntityKeyParam<?>) k.getKeyParam()).getId().equals(entityKeyParam.getId()))
          .forEach(cacheMap::remove);

        executorService.shutdown();
        log.trace("Invalidation for EntityCache in new thread is started");
    }

}
