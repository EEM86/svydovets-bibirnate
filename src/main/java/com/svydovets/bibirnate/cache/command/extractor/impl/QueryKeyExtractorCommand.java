package com.svydovets.bibirnate.cache.command.extractor.impl;


import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkOnIsAssignableTo;
import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkPassedParametersOnNull;

import java.util.Map;
import java.util.Optional;

import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;

import lombok.extern.slf4j.Slf4j;

/**
 * This is the realization of the {@link KeyExtractorCommand} that extracts {@link Key} by {@link QueryKeyParam}.
 */
@Slf4j
public class QueryKeyExtractorCommand implements KeyExtractorCommand {

    public QueryKeyExtractorCommand() {
        log.trace("New QueryKeyExtractorCommand object is created.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Key<?>> executeExtract(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam) {
        log.trace("Start searching QueryKey in cacheMap...");
        checkPassedParametersOnNull(cacheMap, keyParam);

        var queryKeyParam = (QueryKeyParam<?>) checkOnIsAssignableTo(keyParam, QueryKeyParam.class);

        var resultKey = cacheMap.keySet().stream()
          .filter(key -> key.getKeyParam().getClass().isAssignableFrom(QueryKeyParam.class))
          .filter(key -> ((QueryKeyParam<?>) key.getKeyParam()).getEntityType().equals(queryKeyParam.getEntityType()))
          .filter(key -> ((QueryKeyParam<?>) key.getKeyParam()).getCollectionType()
            .equals(queryKeyParam.getCollectionType()))
          .filter(key -> ((QueryKeyParam<?>) key.getKeyParam()).getQuery().equals(queryKeyParam.getQuery()))
          .peek(Key::update)
          .findAny();

        log.trace("Searching QueryKey in cacheMap is finished.");
        return resultKey;
    }

}
