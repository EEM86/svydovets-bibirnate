package com.svydovets.bibirnate.session.impl;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.logs.SqlLogger;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionFactoryImpl implements SessionFactory {

    private static final int DEFAULT_SECOND_CACHE_SIZE = 200_000;
    private final DataSource dataSource;
    private Cache secondLevelCache;
    private final boolean secondLevelCacheEnabled;
    private final int secondLevelCacheSize;
    private final boolean sqlLoggingEnabled;
    private boolean secondLevelCacheGenerated;

    public SessionFactoryImpl(DataSource dataSource, boolean secondLevelCacheEnabled, int secondLevelCacheSize,
                              boolean sqlLoggingEnabled) {
        this.dataSource = dataSource;
        this.secondLevelCacheEnabled = secondLevelCacheEnabled;
        this.secondLevelCacheSize = secondLevelCacheSize;
        this.sqlLoggingEnabled = sqlLoggingEnabled;
        log.info("SessionFactory is successfully configured");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session openSession() {
        log.trace("Session opening is started...");
        if (secondLevelCacheEnabled && !secondLevelCacheGenerated) {
            generateSecondCache();
        }

        try {
            var session = new SessionImpl(
              dataSource.getConnection(),
              new CacheContainer(secondLevelCache, secondLevelCacheEnabled),
              new SqlLogger(sqlLoggingEnabled)
            );

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
        secondLevelCacheGenerated = true;

        log.trace("Second level cache generation is finished.");
    }

}
