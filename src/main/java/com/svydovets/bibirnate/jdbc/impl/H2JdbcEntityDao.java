package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

import com.svydovets.bibirnate.logs.SqlLogger;

/**
 * H2 database implementation.
 */
public class H2JdbcEntityDao extends BaseJdbcEntityDao {

    public H2JdbcEntityDao(Connection connection, SqlLogger sqlLogger) {
        super(connection, sqlLogger);
    }
}
