package com.svydovets.bibirnate.cache.command.invalidation.impl;

import com.svydovets.bibirnate.cache.command.invalidation.InvalidationCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;
import org.apache.commons.lang3.StringUtils;

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
public class QueryKeyInvalidationCommand implements InvalidationCommand {

    private static final String SELECT = "SELECT";
    private static final String INSERT = "INSERT";

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeInvalidate(Map<Key<?>, Object> cacheMap, Key<?> key) {
        // todo: add logger with info that invalidation caches process is started
        checkPassedParametersOnNull(cacheMap, key);

        QueryKeyParam<?> queryKeyParam = (QueryKeyParam<?>) checkOnIsAssignableTo(key.getKeyParam(), QueryKeyParam.class);

        Class<?> entityType = queryKeyParam.getEntityType();
        String query = queryKeyParam.getQuery();

        if (!StringUtils.containsIgnoreCase(query, SELECT)) {
            if (StringUtils.containsIgnoreCase(query, INSERT)) {
                removeAllCacheWithQueryKeyRelated(cacheMap, entityType);
            } else {
                removeAllCacheWithQueryKeyRelated(cacheMap, entityType);
                removeAllCacheWithEntityKeyRelatedToEntityType(cacheMap, entityType);
            }
        }
    }

    private void removeAllCacheWithEntityKeyRelatedToEntityType(Map<Key<?>, Object> cacheMap, Class<?> entityType) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        cacheMap.keySet().stream()
                .filter(k -> k.getKeyParam().getClass().isAssignableFrom(EntityKeyParam.class))
                .filter(k -> k.getKeyParam().getEntityType().isAssignableFrom(entityType))
                .forEach(cacheMap::remove);

        executorService.shutdown();
    }

}
