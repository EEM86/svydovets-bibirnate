package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

import com.svydovets.bibirnate.logs.SqlLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * MySql database implementation.
 */
@Slf4j
public class MysqlJdbcEntityDao extends BaseJdbcEntityDao {

    public MysqlJdbcEntityDao(Connection connection, SqlLogger sqlLogger) {
        super(connection, sqlLogger);
        log.trace("MysqlJdbcEntityDao is successfully configured");
    }
}
