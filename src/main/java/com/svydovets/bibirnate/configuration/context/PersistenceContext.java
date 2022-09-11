package com.svydovets.bibirnate.configuration.context;

import org.apache.logging.log4j.spi.LoggerContext;

import com.svydovets.bibirnate.session.SessionFactory;

public interface PersistenceContext {

    LoggerContext getLoggerContext();

    SessionFactory getSessionFactory();
}
