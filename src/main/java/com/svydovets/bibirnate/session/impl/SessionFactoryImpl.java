package com.svydovets.bibirnate.session.impl;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionFactoryImpl implements SessionFactory {
    private final DataSource dataSource;
    private Cache secondCache;
    private final boolean secondCacheEnabled;

    private final int secondCacheSize;

    private final boolean sqlLoggingEnabled;

    private CacheContainer cacheContainer;


    @Override
    public Session openSession() {
        try {
            return new SessionImpl(dataSource.getConnection(), new CacheContainer(secondCache, secondCacheEnabled));
        } catch (SQLException ex) {
            throw new JdbcException("Cannot open session. The purpose is " + ex.getMessage(), ex);
        }
    }
}
