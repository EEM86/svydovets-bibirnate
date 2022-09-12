package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

public class PostgresJdbcEntityDao extends BaseJdbcEntityDao {

    public PostgresJdbcEntityDao(Connection connection) {
        super(connection);
    }
}
