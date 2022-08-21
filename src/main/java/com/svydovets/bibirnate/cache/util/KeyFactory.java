package com.svydovets.bibirnate.cache.util;

import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;

import java.util.Objects;

public class KeyFactory {

    public static <T> Key<T> generateCacheKey(AbstractKeyParam<T> keyParam) {
        Objects.requireNonNull(keyParam, "The [keyParam] parameter cannot be null!");

        return new Key<>(keyParam);
    }

}
