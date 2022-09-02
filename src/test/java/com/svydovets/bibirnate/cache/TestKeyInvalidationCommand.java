package com.svydovets.bibirnate.cache;

import java.util.Map;

import com.svydovets.bibirnate.cache.command.invalidation.InvalidationCommand;
import com.svydovets.bibirnate.cache.key.Key;

class TestKeyInvalidationCommand implements InvalidationCommand {
    @Override
    public void executeInvalidate(Map<Key<?>, Object> cacheMap, Key<?> key) {
        // this extension uses only for testing
    }
}
