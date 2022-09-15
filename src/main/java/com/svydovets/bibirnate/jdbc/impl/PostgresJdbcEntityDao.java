package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

import com.svydovets.bibirnate.logs.SqlLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * Postgres database implementation.
 */
@Slf4j
public class PostgresJdbcEntityDao extends BaseJdbcEntityDao {

    public PostgresJdbcEntityDao(Connection connection, SqlLogger sqlLogger) {
        super(connection, sqlLogger);
        log.trace("PostgresJdbcEntityDao is successfully configured");
    }
}
