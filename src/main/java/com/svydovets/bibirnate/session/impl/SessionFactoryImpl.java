package com.svydovets.bibirnate.session.impl;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SessionFactoryImpl implements SessionFactory {

    private static final int DEFAULT_SECOND_CACHE_SIZE = 200_000;
    private final DataSource dataSource;
    private Cache secondLevelCache;
    private boolean secondLevelCacheEnabled;
    private boolean secondLevelCacheGenerated;
    private int secondLevelCacheSize;
    private CacheContainer cacheContainer;

    @Override
    public Session openSession() {
        log.trace("Session opening is started...");
        if (secondLevelCacheEnabled && !secondLevelCacheGenerated) {
            generateSecondCache();
        }

        try {
            var session = new SessionImpl(dataSource.getConnection(),
              new CacheContainer(secondLevelCache, secondLevelCacheEnabled));
            log.trace("Session opening is finished. The Session is opened.");
            return session;
        } catch (SQLException ex) {
            log.error("Cannot open Session. Failed with exception [{}]", ex.getMessage());
            throw new JdbcException("Cannot open session. The purpose is " + ex.getMessage(), ex);
        }
    }

    private void generateSecondCache() {
        log.trace("Second level cache generation is started...");

        if (secondLevelCacheSize > 0) {
            this.secondLevelCache = new Cache(secondLevelCacheSize);
        } else {
            this.secondLevelCache = new Cache(DEFAULT_SECOND_CACHE_SIZE);
        }

        log.trace("Second level cache generation is finished.");
    }

}
