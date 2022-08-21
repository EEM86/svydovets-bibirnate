package com.svydovets.bibirnate.cache.command.impl;

import com.svydovets.bibirnate.cache.command.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;

import java.util.Map;
import java.util.Optional;

import static com.svydovets.bibirnate.cache.util.CommandUtil.checkPassedParametersOnNull;
import static com.svydovets.bibirnate.cache.util.CommandUtil.checkOnIsAssignableTo;

public class EntityKeyExtractorCommand implements KeyExtractorCommand {

    public EntityKeyExtractorCommand() {
    }

    @Override
    public Optional<Key<?>> executeExtract(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> abstractKeyParam) {
        checkPassedParametersOnNull(cacheMap, abstractKeyParam);

        EntityKeyParam<?> entityKeyParam = (EntityKeyParam<?>) checkOnIsAssignableTo(abstractKeyParam, EntityKeyParam.class);

        return cacheMap.keySet().stream()
                .filter(key -> key.getKeyParam().getClass().isAssignableFrom(EntityKeyParam.class))
                .filter(key -> ((EntityKeyParam<?>) key.getKeyParam()).getEntityType().equals(entityKeyParam.getEntityType()))
                .filter(key -> ((EntityKeyParam<?>) key.getKeyParam()).getId().equals(entityKeyParam.getId()))
                .peek(Key::update)
                .findAny();
    }

}
