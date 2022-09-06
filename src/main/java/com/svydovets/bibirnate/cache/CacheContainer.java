package com.svydovets.bibirnate.cache;

/**
 * Wraps inside both levels of caches.
 */
public class CacheContainer {
    private final Cache firstLevelCache;
    private final Cache secondLevelCache;
    private final boolean secondLevelCacheEnabled;

    public CacheContainer(Cache secondLevelCache, boolean secondLevelCacheEnabled) {
        firstLevelCache = new Cache();
        this.secondLevelCache = secondLevelCache;
        this.secondLevelCacheEnabled = secondLevelCacheEnabled;
    }

    public Cache getFirstLevelCache() {
        return firstLevelCache;
    }

    public Cache getSecondLevelCache() {
        return secondLevelCache;
    }

    public boolean isSecondLevelCacheEnabled() {
        return secondLevelCacheEnabled;
    }

}
