package com.svydovets.bibirnate.cache;

import static com.svydovets.bibirnate.cache.constant.CacheConstant.INVALIDATE_FIRST_CACHE_FINISH_MESSAGE;
import static com.svydovets.bibirnate.cache.constant.CacheConstant.INVALIDATE_FIRST_CACHE_START_MESSAGE;
import static com.svydovets.bibirnate.cache.constant.CacheConstant.INVALIDATE_SECOND_CACHE_FINISH_MESSAGE;
import static com.svydovets.bibirnate.cache.constant.CacheConstant.INVALIDATE_SECOND_CACHE_START_MESSAGE;
import static com.svydovets.bibirnate.cache.key.factory.KeyParamFactory.generateKeyParam;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import com.svydovets.bibirnate.annotation.Cacheable;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.EntityKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.QueryKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.invalidation.impl.EntityKeyInvalidationCommand;
import com.svydovets.bibirnate.cache.command.invalidation.impl.QueryKeyInvalidationCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

import lombok.extern.slf4j.Slf4j;

/**
 * Provides utils methods for working with double level caches.
 */
@Slf4j
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
        var keyParam = generateKeyParam(entityType, id);

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
        var keyParam = generateKeyParam(entityType, query, collectionType);

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

    /**
     * Provides first {@link Key} with the {@link QueryKeyParam} by provided <b>entityType</b>.
     *
     * @param cacheContainer {@link CacheContainer} that contains both level caches
     * @param entityType     class with entity type
     * @return {@link Optional} of {@link Key}
     */
    public static Optional<Key<?>> getSimilarQueryKey(CacheContainer cacheContainer, Class<?> entityType) {
        var result = cacheContainer.getFirstLevelCache().getKeys().stream()
          .filter(key -> key.getKeyParam().getEntityType().isAssignableFrom(entityType))
          .findFirst();
        if (result.isEmpty() && isSecondCacheEnabledAndUsesForEntity(cacheContainer, entityType)) {
            result = cacheContainer.getSecondLevelCache().getKeys().stream()
              .filter(key -> key.getKeyParam().getEntityType().isAssignableFrom(entityType))
              .findFirst();
        }
        return result;
    }

    /**
     * Invalidates all caches where with the same entity type on both levels.
     *
     * @param cacheContainer {@link CacheContainer} container with caches
     * @param entity         required entity
     */
    public static void invalidate(CacheContainer cacheContainer, Object entity) {
        Cache firstLevelCache = cacheContainer.getFirstLevelCache();
        log.trace(INVALIDATE_FIRST_CACHE_START_MESSAGE);
        invalidate(entity, firstLevelCache);
        log.trace(INVALIDATE_FIRST_CACHE_FINISH_MESSAGE);
        if (isSecondCacheEnabledAndUsesForEntity(cacheContainer, entity.getClass())) {
            log.trace(INVALIDATE_SECOND_CACHE_START_MESSAGE);
            invalidate(entity, cacheContainer.getSecondLevelCache());
            log.trace(INVALIDATE_SECOND_CACHE_FINISH_MESSAGE);
        }
    }

    /**
     * Remove all caches with the same entityType.
     *
     * @param cacheContainer {@link CacheContainer} container with caches
     * @param key            {@link Key}
     */
    public static void invalidate(CacheContainer cacheContainer, Key<?> key) {
        Cache firstLevelCache = cacheContainer.getFirstLevelCache();
        log.trace(INVALIDATE_FIRST_CACHE_START_MESSAGE);
        invalidate(firstLevelCache, key);
        log.trace(INVALIDATE_FIRST_CACHE_FINISH_MESSAGE);
        if (isSecondCacheEnabledAndUsesForEntity(cacheContainer, key.getKeyParam().getEntityType())) {
            log.trace(INVALIDATE_SECOND_CACHE_START_MESSAGE);
            invalidate(cacheContainer.getSecondLevelCache(), key);
            log.trace(INVALIDATE_SECOND_CACHE_FINISH_MESSAGE);
        }
    }

    private static <T> void invalidate(T entity, Cache cache) {
        var entityType = entity.getClass();
        if (entityType.isAnnotationPresent(Entity.class)) {
            var id = Arrays.stream(entityType.getDeclaredFields())
              .filter(field -> field.isAnnotationPresent(Id.class))
              .findAny()
              .map(getFieldValueFunction(entity));
            if (id.isPresent()) {
                var keyParam = generateKeyParam(entityType, id.get());
                cache.invalidateRelated(keyParam, EntityKeyExtractorCommand.class, EntityKeyInvalidationCommand.class);
            }
        }
    }

    private static <T> void invalidate(Cache cache, Key<?> key) {
        cache.invalidateRelated(key.getKeyParam(), QueryKeyExtractorCommand.class, QueryKeyInvalidationCommand.class);
    }

    private static Function<Field, Object> getFieldValueFunction(Object entity) {
        return field -> {
            try {
                field.setAccessible(true);
                return field.get(entity);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Failed on extraction field value for invalidation caches with message ["
                  + ex.getMessage() + "].");
            }
        };
    }

    private static <T> Optional<Object> getCachedObject(CacheContainer cacheContainer, Class<T> entityType,
                                                        AbstractKeyParam<T> keyParam,
                                                        Class<? extends KeyExtractorCommand> extractorType) {
        var cached = cacheContainer.getFirstLevelCache().get(keyParam, extractorType);

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
