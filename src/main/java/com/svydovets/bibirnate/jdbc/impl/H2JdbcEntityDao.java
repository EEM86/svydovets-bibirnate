package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

public class H2JdbcEntityDao extends BaseJdbcEntityDao {

    public H2JdbcEntityDao(Connection connection) {
        super(connection);
    }
}
