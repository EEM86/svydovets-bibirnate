package com.svydovets.bibirnate.session.query;


/**
 * Defines the set of cascadable operations that are propagated to the associated entity.
 * The value cascade=ALL is equivalent to cascade={PERSIST, DELETE}.
 *
 *<p>Example:
 *
 *<p>ManyToOne(cascade=CascadeType.ALL)
 * protected String getName() {return name;}
 */
public enum CascadeType {

    /**
     * Cascade persist operations.
     */
    PERSIST,
    /**
     * Cascade delete operations.
     */
    DELETE,
    /**
     * Cascade all operations.
     */
    ALL
}
