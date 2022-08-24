package com.svydovets.bibirnate.cache.util;

import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.svydovets.bibirnate.cache.constant.CacheConstant.PARAMETER_CANNOT_BE_NULL;

/**
 * Provides util methods for working with commands.
 */
public class CommandUtil {

    private CommandUtil() {
    }

    /**
     * Check if passed parameters are null. If some of them is null then throw {@link NullPointerException}.
     *
     * @param cacheMap {@link Map} with cache
     * @param keyParam instance of the {@link AbstractKeyParam}
     */
    public static void checkPassedParametersOnNull(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam) {
        Objects.requireNonNull(cacheMap, String.format(PARAMETER_CANNOT_BE_NULL, cacheMap));
        Objects.requireNonNull(keyParam, String.format(PARAMETER_CANNOT_BE_NULL, keyParam));
    }

    /**
     * Check if passed parameters are null. If some of them is null then throw {@link NullPointerException}.
     *
     * @param cacheMap {@link Map} with cache
     * @param key      {@link Key}
     */
    public static void checkPassedParametersOnNull(Map<Key<?>, Object> cacheMap, Key<?> key) {
        Objects.requireNonNull(cacheMap, String.format(PARAMETER_CANNOT_BE_NULL, cacheMap));
        Objects.requireNonNull(key, String.format(PARAMETER_CANNOT_BE_NULL, key));
    }

    /**
     * Check on is assignable from. If assignable then cast it and return, otherwise throws
     * {@link IllegalArgumentException}.
     *
     * @param keyParam     instance of the {@link AbstractKeyParam}
     * @param keyParamType {@link Class} of the keyParam type
     * @return instance of the {@link AbstractKeyParam} or throws {@link IllegalArgumentException}
     */
    public static AbstractKeyParam<?> checkOnIsAssignableTo(AbstractKeyParam<?> keyParam,
                                                            Class<? extends AbstractKeyParam> keyParamType) {
        if (keyParam.getClass().isAssignableFrom(keyParamType)) {
            return keyParamType.cast(keyParam);
        } else {
            throw new IllegalArgumentException(
                    String.format("The [keyParam] parameter has a wrong type! It should be [%s]" +
                            "but was [%s]", keyParamType, keyParam.getClass()));
        }
    }

    /**
     * Provides invalidation for all cache that have in {@link Key}{@link AbstractKeyParam} the same entityType
     * ({@link AbstractKeyParam#getEntityType()}.
     *
     * @param cacheMap   {@link Map} with cache
     * @param entityType type of entity
     */
    public static void removeAllCacheWithQueryKeyRelated(Map<Key<?>, Object> cacheMap, Class<?> entityType) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        cacheMap.keySet().stream()
                .filter(k -> k.getKeyParam().getClass().isAssignableFrom(QueryKeyParam.class))
                .filter(k -> k.getKeyParam().getEntityType().isAssignableFrom(entityType))
                .forEach(cacheMap::remove);

        executorService.shutdown();
    }

}
