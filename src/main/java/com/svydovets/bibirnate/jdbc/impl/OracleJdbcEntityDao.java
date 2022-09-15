package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

import com.svydovets.bibirnate.logs.SqlLogger;

/**
 * Oracle database implementation.
 */
public class OracleJdbcEntityDao extends BaseJdbcEntityDao {

    public OracleJdbcEntityDao(Connection connection, SqlLogger sqlLogger) {
        super(connection, sqlLogger);
    }
}
