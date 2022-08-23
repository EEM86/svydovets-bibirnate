package com.svydovets.bibirnate.session;

import javax.sql.DataSource;

/**
 * This is main interface to create {@link Session}.
 */
public interface SessionFactory {
    /**
     * Creates {@link Session} for provided {@link DataSource}.
     */
    Session createSession(DataSource dataSource);
}
