package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

import com.svydovets.bibirnate.logs.SqlLogger;

/**
 * MySql database implementation.
 */
public class MysqlJdbcEntityDao extends BaseJdbcEntityDao {

    public MysqlJdbcEntityDao(Connection connection, SqlLogger sqlLogger) {
        super(connection, sqlLogger);
    }
}
