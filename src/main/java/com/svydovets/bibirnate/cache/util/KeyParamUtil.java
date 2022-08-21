package com.svydovets.bibirnate.cache.util;

import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

import java.util.Collection;
import java.util.Objects;

public class KeyParamUtil {

    public static <T> AbstractKeyParam<T> generateKeyParam(Class<T> entityType, Object id) {
        Objects.requireNonNull(entityType, "The [entityType] parameter cannot be null!");
        Objects.requireNonNull(id, "The [id] parameter cannot be null!");

        return new EntityKeyParam<>(entityType, id);
    }

    public static <T> AbstractKeyParam<T> generateKeyParam(Class<T> entityType, String query,
                                                           Class<? extends Collection> collectionType) {
        Objects.requireNonNull(entityType, "The [entityType] parameter cannot be null!");
        Objects.requireNonNull(query, "The [query] parameter cannot be null!");
        Objects.requireNonNull(collectionType, "The [collectionType] parameter cannot be null!");

        return new QueryKeyParam<>(entityType, query, collectionType);
    }

}
