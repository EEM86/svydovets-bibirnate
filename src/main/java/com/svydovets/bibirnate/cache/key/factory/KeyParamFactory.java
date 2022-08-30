package com.svydovets.bibirnate.cache.key.factory;

import static com.svydovets.bibirnate.cache.constant.CacheConstant.PARAMETER_CANNOT_BE_NULL;

import java.util.Collection;
import java.util.Objects;

import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

/**
 * Provides overloaded methods for creating instances of the {@link AbstractKeyParam}.
 */
public class KeyParamFactory {

    /**
     * Provides instance of the {@link EntityKeyParam}. In case if some of passes parameters is null then throw
     * {@link NullPointerException}.
     *
     * @param entityType entity class
     * @param id         entity id
     * @param <T>        type of entity
     * @return {@link EntityKeyParam}
     */
    public static <T> AbstractKeyParam<T> generateKeyParam(Class<T> entityType, Object id) {
        Objects.requireNonNull(entityType, String.format(PARAMETER_CANNOT_BE_NULL, entityType));
        Objects.requireNonNull(id, String.format(PARAMETER_CANNOT_BE_NULL, id));

        return new EntityKeyParam<>(entityType, id);
    }

    /**
     * Provides instance of the {@link QueryKeyParam}. In case if some of passes parameters is null then throw
     * {@link NullPointerException}.
     *
     * @param entityType     entity class
     * @param query          {@link String} with query for extracting result from DB
     * @param collectionType collection class
     * @param <T>            type of entity
     * @return {@link QueryKeyParam}
     */
    public static <T> AbstractKeyParam<T> generateKeyParam(Class<T> entityType, String query,
                                                           Class<? extends Collection> collectionType) {
        Objects.requireNonNull(entityType, String.format(PARAMETER_CANNOT_BE_NULL, entityType));
        Objects.requireNonNull(query, String.format(PARAMETER_CANNOT_BE_NULL, query));
        Objects.requireNonNull(collectionType, String.format(PARAMETER_CANNOT_BE_NULL, collectionType));

        return new QueryKeyParam<>(entityType, query, collectionType);
    }

}
