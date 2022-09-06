package com.svydovets.bibirnate.cache;

import static com.svydovets.bibirnate.cache.key.factory.KeyParamFactory.generateKeyParam;

import java.util.Collection;
import java.util.Optional;

import com.svydovets.bibirnate.annotation.Cacheable;
import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.EntityKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.QueryKeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;

/**
 * Provides utils methods for working with double level caches.
 */
public final class CacheUtils {

    private CacheUtils() {
    }

    /**
     * Extracts from both level caches a cached entity.
     *
     * @param cacheContainer {@link CacheContainer} that contains both level caches
     * @param id             entity id
     * @param entityType     class with entity type
     * @param <T>            entity type
     * @return {@link Optional} of value from cache
     */
    public static <T> Optional<T> extract(CacheContainer cacheContainer, Class<T> entityType, Object id) {
        AbstractKeyParam<T> keyParam = generateKeyParam(entityType, id);

        return getCachedObject(cacheContainer, entityType, keyParam, EntityKeyExtractorCommand.class)
          .filter(cached -> entityType.isAssignableFrom(cached.getClass()))
          .map(entityType::cast);
    }

    /**
     * Extracts from both level caches a cached collection.
     *
     * @param cacheContainer {@link CacheContainer} that contains both level caches
     * @param entityType     class with entity type
     * @param query          {@link String} with original query to database
     * @param collectionType type of collection
     * @param <T>            entity type
     * @return {@link Optional} of value from cache
     */
    public static <T> Optional<? extends Collection> extract(CacheContainer cacheContainer, Class<T> entityType,
                                                             String query,
                                                             Class<? extends Collection> collectionType) {
        AbstractKeyParam<T> keyParam = generateKeyParam(entityType, query, collectionType);

        return getCachedObject(cacheContainer, entityType, keyParam, QueryKeyExtractorCommand.class)
          .filter(cached -> collectionType.isAssignableFrom(cached.getClass()))
          .map(collectionType::cast);
    }

    /**
     * Put to both cashes (to second level in case if enabled) an entity value.
     *
     * @param cacheContainer {@link CacheContainer} that contains both level caches
     * @param id             entity id
     * @param entityType     class with entity type
     * @param value          value that should be cached
     * @param <T>            entity type
     */
    public static <T> void put(CacheContainer cacheContainer, Class<T> entityType, Object id, Object value) {
        put(cacheContainer, generateKeyParam(entityType, id), value);
    }

    /**
     * Put to both cashes (to second level in case if enabled) a collection value.
     *
     * @param cacheContainer {@link CacheContainer} that contains both level caches
     * @param query          {@link String} with original query to database
     * @param collectionType type of collection
     * @param value          value that should be cached
     * @param <T>            entity type
     */
    public static <T> void put(CacheContainer cacheContainer, Class<T> entityType, String query,
                               Class<? extends Collection> collectionType, Object value) {
        put(cacheContainer, generateKeyParam(entityType, query, collectionType), value);
    }

    private static <T> void put(CacheContainer cacheContainer, AbstractKeyParam<T> keyParam, Object value) {
        cacheContainer.getFirstLevelCache().put(keyParam, value);
        if (isSecondCacheEnabledAndUsesForEntity(cacheContainer, keyParam.getEntityType())) {
            cacheContainer.getSecondLevelCache().put(keyParam, value);
        }
    }

    // TODO: provide api for invalidation

    private static <T> Optional<Object> getCachedObject(CacheContainer cacheContainer, Class<T> entityType,
                                                        AbstractKeyParam<T> keyParam,
                                                        Class<? extends KeyExtractorCommand> extractorType) {
        Optional<Object> cached = cacheContainer.getFirstLevelCache().get(keyParam, extractorType);

        if (cached.isEmpty() && isSecondCacheEnabledAndUsesForEntity(cacheContainer, entityType)) {
            cached = cacheContainer.getSecondLevelCache().get(keyParam, extractorType);
        }

        return cached;
    }

    private static <T> boolean isSecondCacheEnabledAndUsesForEntity(CacheContainer cacheContainer,
                                                                    Class<T> entityType) {
        if (cacheContainer.isSecondLevelCacheEnabled()) {
            return entityType.isAnnotationPresent(Cacheable.class) && entityType.getAnnotation(Cacheable.class).value();
        }
        return false;
    }

}
