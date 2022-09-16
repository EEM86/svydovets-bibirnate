package com.svydovets.bibirnate.session.query;

/**
 * Defines strategies for fetching data from the database.
 * The EAGER strategy means that the data is fully loaded from the database.
 * The LAZY strategy means that the data will be loaded from the database on the first access request.
 * Example:
 * ManyToOne(fetch=LAZY)
 * protected String getName() {return name;}
 */
public enum FetchType {
    LAZY, EAGER
}
