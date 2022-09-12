package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

public class MysqlJdbcEntityDao extends BaseJdbcEntityDao {

    public MysqlJdbcEntityDao(Connection connection) {
        super(connection);
    }
}
