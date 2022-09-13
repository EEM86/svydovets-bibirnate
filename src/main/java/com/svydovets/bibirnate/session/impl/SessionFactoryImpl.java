package com.svydovets.bibirnate.session.impl;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionFactoryImpl implements SessionFactory {
    private final DataSource dataSource;
    //todo: need to provide configuration for secondLevelCache and size for it
    private final CacheContainer cacheContainer;

    @Override
    public Session openSession() {
        try {
            cacheContainer = new CacheContainer(new Cache(), secondCacheEnabled);
            return new SessionImpl(dataSource.getConnection(), cacheContainer);
        } catch (SQLException ex) {
            throw new JdbcException("Cannot open session. The purpose is " + ex.getMessage(), ex);
        }
    }
}
