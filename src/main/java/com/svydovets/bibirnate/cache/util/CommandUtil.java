package com.svydovets.bibirnate.cache.util;

import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandUtil {

    private CommandUtil() {
    }

    public static void checkPassedParametersOnNull(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> abstractKeyParam) {
        Objects.requireNonNull(cacheMap, "The [cacheMap] parameter cannot be null!");
        Objects.requireNonNull(abstractKeyParam, "The [abstractKeyParam] parameter cannot be null!");
    }

    public static void checkPassedParametersOnNull(Map<Key<?>, Object> cacheMap, Key<?> key) {
        Objects.requireNonNull(cacheMap, "The [cacheMap] parameter cannot be null!");
        Objects.requireNonNull(key, "The [key] parameter cannot be null!");
    }

    public static AbstractKeyParam<?> checkOnIsAssignableTo(AbstractKeyParam<?> abstractKeyParam,
                                                            Class<? extends AbstractKeyParam> keyParamType) {
        if (abstractKeyParam.getClass().isAssignableFrom(keyParamType)) {
            return abstractKeyParam;
        } else {
            throw new IllegalArgumentException(
                    String.format("The [abstractKeyParam] parameter has a wrong type! It should be [%s]" +
                            "but was [%s]", keyParamType, abstractKeyParam.getClass()));
        }
    }

    public static void removeAllCacheWithQueryKeyRelated(Map<Key<?>, Object> cacheMap, Class<?> entityType) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        cacheMap.keySet().stream()
                .filter(k -> k.getKeyParam().getClass().isAssignableFrom(QueryKeyParam.class))
                .filter(k -> k.getKeyParam().getEntityType().isAssignableFrom(entityType))
                .forEach(cacheMap::remove);

        executorService.shutdown();
    }


}
