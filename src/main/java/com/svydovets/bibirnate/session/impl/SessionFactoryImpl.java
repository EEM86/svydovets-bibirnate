package com.svydovets.bibirnate.session.impl;

import javax.sql.DataSource;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;

import lombok.RequiredArgsConstructor;

public class SessionFactoryImpl implements SessionFactory {
    private final DataSource dataSource;
    private final Cache cache;
    private static boolean secondLevelCacheEnabled;

    public SessionFactoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.cache = new Cache(40_000);
    }

    @Override
    public Session openSession() {
        return new SessionImpl(dataSource, cache);
    }

    public static boolean isSecondLevelCacheEnabled() {
        return secondLevelCacheEnabled;
    }

    public void enableSecondLevelCache() {
        secondLevelCacheEnabled = true;
    }

    public void disableSecondLevelCache() {
        secondLevelCacheEnabled = false;
    }



}
