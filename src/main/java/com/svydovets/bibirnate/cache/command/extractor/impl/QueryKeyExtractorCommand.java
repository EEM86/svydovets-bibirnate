package com.svydovets.bibirnate.cache.command.extractor.impl;

import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

import java.util.Map;
import java.util.Optional;

import static com.svydovets.bibirnate.cache.util.CommandUtil.checkOnIsAssignableTo;
import static com.svydovets.bibirnate.cache.util.CommandUtil.checkPassedParametersOnNull;

public class QueryKeyExtractorCommand implements KeyExtractorCommand {

    public QueryKeyExtractorCommand() {
    }

    @Override
    public Optional<Key<?>> executeExtract(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> abstractKeyParam) {
        checkPassedParametersOnNull(cacheMap, abstractKeyParam);

        QueryKeyParam<?> queryKeyParam = (QueryKeyParam<?>) checkOnIsAssignableTo(abstractKeyParam, QueryKeyParam.class);

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
