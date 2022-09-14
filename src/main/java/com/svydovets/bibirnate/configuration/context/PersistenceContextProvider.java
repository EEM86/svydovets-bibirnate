package com.svydovets.bibirnate.configuration.context;

import com.svydovets.bibirnate.session.SessionFactory;
import com.svydovets.bibirnate.session.impl.builder.SessionFactoryBuilder;

import lombok.NoArgsConstructor;

/**
 * Entry point for working with Bibernate ORM. The main purpose is to create SessionFactory that can open connection
 * sessions to database.Every time createSessionFactory() is called new SessionFactory will be provided
 * based on SessionFactoryBuilder options. To be able properly use second level cache SessionFactory instance must be
 * shared by application.
 *
 */
@NoArgsConstructor
public class PersistenceContextProvider {
    /**
     * This method creates SessionFactory from provided SessionFactoryBuilder.
     * {@param  SessionFactoryBuilder}   - builder interface for building SessionFactory. Has two implementations:
     * DefaultSessionFactoryBuilderImpl {@link new DefaultSessionFactoryBuilderImpl}- configuration provided
     * through Java; YamlConfigurationSessionFactoryBuilderImpl {@link new YamlConfigurationSessionFactoryBuilderImpl}
     * - configuration provided through YAML properties file
     *
     * @return SessionFactory - create and return an implementation of SessionFactory
     */
    public static SessionFactory createSessionFactory(SessionFactoryBuilder sessionFactoryBuilder) {
        return sessionFactoryBuilder.build();
    }
}
