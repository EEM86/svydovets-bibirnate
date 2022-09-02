package com.svydovets.bibirnate.cache;

import java.util.Map;
import java.util.Optional;

import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;

class TestKeyExtractorCommand implements KeyExtractorCommand {
    @Override
    public Optional<Key<?>> executeExtract(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam) {
        // this extension uses only for testing
        return Optional.empty();
    }
}
