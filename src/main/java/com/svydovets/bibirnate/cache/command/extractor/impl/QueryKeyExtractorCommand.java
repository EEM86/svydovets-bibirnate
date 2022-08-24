package com.svydovets.bibirnate.cache.command.extractor.impl;

import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

import java.util.Map;
import java.util.Optional;

import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkOnIsAssignableTo;
import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkPassedParametersOnNull;

/**
 * This is the realization of the {@link KeyExtractorCommand} that extracts {@link Key} by {@link QueryKeyParam}.
 */
public class QueryKeyExtractorCommand implements KeyExtractorCommand {

    public QueryKeyExtractorCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Key<?>> executeExtract(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam) {
        checkPassedParametersOnNull(cacheMap, keyParam);

        QueryKeyParam<?> queryKeyParam = (QueryKeyParam<?>) checkOnIsAssignableTo(keyParam, QueryKeyParam.class);

        return cacheMap.keySet().stream()
                .filter(key -> key.getKeyParam().getClass().isAssignableFrom(QueryKeyParam.class))
                .filter(key -> ((QueryKeyParam<?>) key.getKeyParam()).getEntityType()
                        .equals(queryKeyParam.getEntityType()))
                .filter(key -> ((QueryKeyParam<?>) key.getKeyParam()).getCollectionType()
                        .equals(queryKeyParam.getCollectionType()))
                .filter(key -> ((QueryKeyParam<?>) key.getKeyParam()).getQuery().equals(queryKeyParam.getQuery()))
                .peek(Key::update)
                .findAny();
    }

}
