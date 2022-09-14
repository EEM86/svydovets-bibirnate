package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

import com.svydovets.bibirnate.logs.SqlLogger;

/**
 * Postgres database implementation.
 */
public class PostgresJdbcEntityDao extends BaseJdbcEntityDao {

    public PostgresJdbcEntityDao(Connection connection, SqlLogger sqlLogger) {
        super(connection, sqlLogger);
    }
}
