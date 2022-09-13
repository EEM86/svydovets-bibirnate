package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

/**
 * H2 database implementation.
 */
public class H2JdbcEntityDao extends BaseJdbcEntityDao {

    public H2JdbcEntityDao(Connection connection) {
        super(connection);
    }
}
