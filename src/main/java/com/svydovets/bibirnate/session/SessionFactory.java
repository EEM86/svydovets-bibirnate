package com.svydovets.bibirnate.session;

/**
 * This is main interface to create {@link Session}.
 */
public interface SessionFactory {
    /**
     * Creates {@link Session} which provides ability to work with {@link com.svydovets.bibirnate.annotation.Entity}.
     */
    Session openSession();
}
