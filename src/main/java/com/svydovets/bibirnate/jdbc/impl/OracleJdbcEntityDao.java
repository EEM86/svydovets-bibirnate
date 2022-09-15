package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

import com.svydovets.bibirnate.logs.SqlLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * Oracle database implementation.
 */
@Slf4j
public class OracleJdbcEntityDao extends BaseJdbcEntityDao {

    public OracleJdbcEntityDao(Connection connection, SqlLogger sqlLogger) {
        super(connection, sqlLogger);
        log.trace("OracleJdbcEntityDao is successfully configured");
    }
}
