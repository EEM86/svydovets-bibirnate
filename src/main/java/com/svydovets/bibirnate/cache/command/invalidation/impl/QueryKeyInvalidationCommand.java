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
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

import lombok.extern.slf4j.Slf4j;


/**
 * This is the realization of the {@link InvalidationCommand} that makes invalidation for caches related to passed
 * {@link Key}.
 */
@Slf4j
public class QueryKeyInvalidationCommand implements InvalidationCommand {

    public QueryKeyInvalidationCommand() {
        log.trace("New QueryKeyInvalidationCommand object is created.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeInvalidate(Map<Key<?>, Object> cacheMap, Key<?> key) {
        log.trace("Cache invalidation started for QueryCache");
        checkPassedParametersOnNull(cacheMap, key);

        var queryKeyParam = (QueryKeyParam<?>) checkOnIsAssignableTo(key.getKeyParam(), QueryKeyParam.class);
        var entityType = queryKeyParam.getEntityType();

        removeAllCacheWithQueryKeyRelated(cacheMap, entityType);
        removeAllCacheWithEntityKeyRelatedToEntityType(cacheMap, entityType);
    }

    private void removeAllCacheWithEntityKeyRelatedToEntityType(Map<Key<?>, Object> cacheMap, Class<?> entityType) {
        log.trace("Starting invalidation for QueryCache in new thread...");
        var executorService = Executors.newSingleThreadExecutor();
        cacheMap.keySet().stream()
          .filter(k -> k.getKeyParam().getClass().isAssignableFrom(EntityKeyParam.class))
          .filter(k -> k.getKeyParam().getEntityType().isAssignableFrom(entityType))
          .forEach(cacheMap::remove);

        executorService.shutdown();
        log.trace("Invalidation for QueryCache in new thread is started");
    }

}
