package com.svydovets.bibirnate.cache.command.util;

import static com.svydovets.bibirnate.cache.constant.CacheConstant.CHECK_PASSED_PARAMETERS_ON_NULL_FINISH_MESSAGE;
import static com.svydovets.bibirnate.cache.constant.CacheConstant.CHECK_PASSED_PARAMETERS_ON_NULL_START_MESSAGE;
import static com.svydovets.bibirnate.cache.constant.CacheConstant.PARAMETER_CANNOT_BE_NULL;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

import lombok.extern.slf4j.Slf4j;

/**
 * Provides util methods for working with commands.
 */
@Slf4j
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
        log.trace(CHECK_PASSED_PARAMETERS_ON_NULL_START_MESSAGE);
        Objects.requireNonNull(cacheMap, String.format(PARAMETER_CANNOT_BE_NULL, "cacheMap"));
        Objects.requireNonNull(keyParam, String.format(PARAMETER_CANNOT_BE_NULL, "keyParam"));
        log.trace(CHECK_PASSED_PARAMETERS_ON_NULL_FINISH_MESSAGE);
    }

    /**
     * Check if passed parameters are null. If some of them is null then throw {@link NullPointerException}.
     *
     * @param cacheMap {@link Map} with cache
     * @param key      {@link Key}
     */
    public static void checkPassedParametersOnNull(Map<Key<?>, Object> cacheMap, Key<?> key) {
        log.trace(CHECK_PASSED_PARAMETERS_ON_NULL_START_MESSAGE);
        Objects.requireNonNull(cacheMap, String.format(PARAMETER_CANNOT_BE_NULL, "cacheMap"));
        Objects.requireNonNull(key, String.format(PARAMETER_CANNOT_BE_NULL, "key"));
        log.trace(CHECK_PASSED_PARAMETERS_ON_NULL_FINISH_MESSAGE);
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
        log.trace("checkOnIsAssignableTo, start validation that [{}#entityType] is assignable to [{}].",
          keyParam.toString(), keyParamType.getSimpleName());
        if (keyParam.getClass().isAssignableFrom(keyParamType)) {
            log.trace("checkOnIsAssignableTo, [{}#entityType] is assignable to [{}].", keyParam,
              keyParamType.getName());
            return keyParamType.cast(keyParam);
        } else {
            log.error("checkOnIsAssignableTo, validation that [{}#entityType] is assignable to [{}] FAILED.", keyParam,
              keyParamType.getSimpleName());
            throw new IllegalArgumentException(
              String.format("The [keyParam] parameter has a wrong type! It should be [%s] but was [%s]", keyParamType,
                keyParam.getClass()));
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
