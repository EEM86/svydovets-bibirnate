package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

/**
 * MySql database implementation.
 */
public class MysqlJdbcEntityDao extends BaseJdbcEntityDao {

    public MysqlJdbcEntityDao(Connection connection) {
        super(connection);
    }
}
