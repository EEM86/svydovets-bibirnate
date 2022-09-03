package com.svydovets.bibirnate.session.impl;

import javax.sql.DataSource;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionFactoryImpl implements SessionFactory {
    private final DataSource dataSource;
    //todo: need to provide configuration for secondLevelCache and size for it
    private Cache secondCache;
    private boolean secondCacheEnabled;
    private CacheContainer cacheContainer;

    @Override
    public Session openSession() {
        return new SessionImpl(dataSource, cacheContainer);
    }

    public void setEnabled(boolean enabled) {
        secondCacheEnabled = enabled;
    }

    public void initSecondCache(int size) {
        this.secondCache = new Cache(size);
    }

    public Cache getSecondCache() {
        return secondCache;
    }

}
