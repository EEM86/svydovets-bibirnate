package com.svydovets.bibirnate.session.query;

/**
 * Defines the set of possible operations to interact with database.
 */
public enum CrudOperation {
    /**
     * Operation related to creation a new instance. like SQL INSERT.
     */
    CREATE,
    /**
     * Operation related to readonly interacting with existing data. like SQL SELECT.
     */
    READ,
    /**
     * Operation related to adding changes to existing objects.
     */
    UPDATE,
    /**
     * Operation related to removing existing objects.
     */
    DELETE
}
