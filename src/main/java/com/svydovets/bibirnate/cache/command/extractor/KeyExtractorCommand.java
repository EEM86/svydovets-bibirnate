package com.svydovets.bibirnate.cache.command.extractor;

import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;

import java.util.Map;
import java.util.Optional;

public interface KeyExtractorCommand {

    Optional<Key<?>> executeExtract(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> abstractKeyParam);

}
