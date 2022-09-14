package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

/**
 * Oracle database implementation.
 */
public class OracleJdbcEntityDao extends BaseJdbcEntityDao {

    public OracleJdbcEntityDao(Connection connection) {
        super(connection);
    }
}
