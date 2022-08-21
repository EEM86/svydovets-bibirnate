package com.svydovets.bibirnate.cache.command.invalidation;

import com.svydovets.bibirnate.cache.key.Key;

import java.util.Map;

public interface InvalidationCommand {

    void executeInvalidate(Map<Key<?>, Object> cacheMap, Key<?> key);

}
