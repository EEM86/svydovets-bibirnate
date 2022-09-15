package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

import com.svydovets.bibirnate.logs.SqlLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * H2 database implementation.
 */
@Slf4j
public class H2JdbcEntityDao extends BaseJdbcEntityDao {

    public H2JdbcEntityDao(Connection connection, SqlLogger sqlLogger) {
        super(connection, sqlLogger);
        log.trace("H2JdbcEntityDao is successfully configured");
    }
}
