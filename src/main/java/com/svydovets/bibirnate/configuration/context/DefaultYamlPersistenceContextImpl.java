package com.svydovets.bibirnate.configuration.context;

import org.apache.logging.log4j.spi.LoggerContext;

import com.svydovets.bibirnate.session.SessionFactory;

public class DefaultYamlPersistenceContextImpl implements PersistenceContext {

    private final SessionFactory sessionFactory;
    private final LoggerContext loggerContext;

    public DefaultYamlPersistenceContextImpl(SessionFactory sessionFactory, LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public LoggerContext getLoggerContext() {
        return loggerContext;
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
