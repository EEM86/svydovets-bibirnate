package com.svydovets.bibirnate.cache.key.factory;

import static com.svydovets.bibirnate.cache.constant.CacheConstant.GENERATE_KEY_PARAM_FINISH_MESSAGE;
import static com.svydovets.bibirnate.cache.constant.CacheConstant.GENERATE_KEY_PARAM_VALIDATION_FINISH_MESSAGE;
import static com.svydovets.bibirnate.cache.constant.CacheConstant.GENERATE_KEY_PARAM_VALIDATION_START_MESSAGE;
import static com.svydovets.bibirnate.cache.constant.CacheConstant.PARAMETER_CANNOT_BE_NULL;

import java.util.Collection;
import java.util.Objects;

import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

import lombok.extern.slf4j.Slf4j;

/**
 * Provides overloaded methods for creating instances of the {@link AbstractKeyParam}.
 */
@Slf4j
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
        log.trace(GENERATE_KEY_PARAM_VALIDATION_START_MESSAGE);
        Objects.requireNonNull(entityType, String.format(PARAMETER_CANNOT_BE_NULL, entityType));
        Objects.requireNonNull(id, String.format(PARAMETER_CANNOT_BE_NULL, id));
        log.trace(GENERATE_KEY_PARAM_VALIDATION_FINISH_MESSAGE);

        var entityKeyParam = new EntityKeyParam<>(entityType, id);

        log.trace(GENERATE_KEY_PARAM_FINISH_MESSAGE, entityKeyParam);
        return entityKeyParam;
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
        log.trace(GENERATE_KEY_PARAM_VALIDATION_START_MESSAGE);
        Objects.requireNonNull(entityType, String.format(PARAMETER_CANNOT_BE_NULL, entityType));
        Objects.requireNonNull(query, String.format(PARAMETER_CANNOT_BE_NULL, query));
        Objects.requireNonNull(collectionType, String.format(PARAMETER_CANNOT_BE_NULL, collectionType));
        log.trace(GENERATE_KEY_PARAM_VALIDATION_FINISH_MESSAGE);

        var queryKeyParam = new QueryKeyParam<>(entityType, query, collectionType);

        log.trace(GENERATE_KEY_PARAM_FINISH_MESSAGE, queryKeyParam);
        return queryKeyParam;
    }

}
