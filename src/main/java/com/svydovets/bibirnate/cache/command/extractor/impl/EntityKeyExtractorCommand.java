package com.svydovets.bibirnate.cache.command.extractor.impl;

import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkOnIsAssignableTo;
import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkPassedParametersOnNull;

import java.util.Map;
import java.util.Optional;

import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;

import lombok.extern.slf4j.Slf4j;

/**
 * This is the realization of the {@link KeyExtractorCommand} that extracts {@link Key} by {@link EntityKeyParam}.
 */
@Slf4j
public class EntityKeyExtractorCommand implements KeyExtractorCommand {

    public EntityKeyExtractorCommand() {
        log.trace("New EntityKeyExtractorCommand object is created.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Key<?>> executeExtract(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam) {
        log.trace("Start searching EntityKey in cacheMap...");
        checkPassedParametersOnNull(cacheMap, keyParam);

        var entityKeyParam = (EntityKeyParam<?>) checkOnIsAssignableTo(keyParam, EntityKeyParam.class);

        var resultKey = cacheMap.keySet().stream()
          .filter(key -> key.getKeyParam().getClass().isAssignableFrom(EntityKeyParam.class))
          .filter(key -> ((EntityKeyParam<?>) key.getKeyParam()).getEntityType().equals(entityKeyParam.getEntityType()))
          .filter(key -> ((EntityKeyParam<?>) key.getKeyParam()).getId().equals(entityKeyParam.getId()))
          .peek(Key::update)
          .findAny();

        log.trace("Searching EntityKey in cacheMap is finished.");
        return resultKey;
    }

}
