package com.svydovets.bibirnate.session.impl.builder;

import com.svydovets.bibirnate.session.SessionFactory;

/**
 * Provides builder for the {@link SessionFactory}.
 */
public interface SessionFactoryBuilder {

    /**
     * Generates new {@link SessionFactory} from provided parameters.
     *
     * @return {@link SessionFactory}
     */
    SessionFactory build();

}
