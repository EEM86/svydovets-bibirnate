package com.svydovets.bibirnate.session;

import com.svydovets.bibirnate.annotation.Entity;

/**
 * This is main interface to create {@link Session}.
 */
public interface SessionFactory {

    /**
     * Creates {@link Session} which provides ability to work with {@link Entity}.
     */
    Session openSession();
}
