package com.svydovets.bibirnate.cache.command.extractor.impl;

import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;

import java.util.Map;
import java.util.Optional;

import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkOnIsAssignableTo;
import static com.svydovets.bibirnate.cache.command.util.CommandUtil.checkPassedParametersOnNull;

/**
 * This is the realization of the {@link KeyExtractorCommand} that extracts {@link Key} by {@link EntityKeyParam}.
 */
public class EntityKeyExtractorCommand implements KeyExtractorCommand {

    public EntityKeyExtractorCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Key<?>> executeExtract(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam) {
        checkPassedParametersOnNull(cacheMap, keyParam);

        EntityKeyParam<?> entityKeyParam = (EntityKeyParam<?>) checkOnIsAssignableTo(keyParam, EntityKeyParam.class);

        return cacheMap.keySet().stream()
                .filter(key -> key.getKeyParam().getClass().isAssignableFrom(EntityKeyParam.class))
                .filter(key -> ((EntityKeyParam<?>) key.getKeyParam()).getEntityType().equals(entityKeyParam.getEntityType()))
                .filter(key -> ((EntityKeyParam<?>) key.getKeyParam()).getId().equals(entityKeyParam.getId()))
                .peek(Key::update)
                .findAny();
    }

}
