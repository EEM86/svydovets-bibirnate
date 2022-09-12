package com.svydovets.bibirnate.jdbc.impl;

import java.sql.Connection;

public class OracleJdbcEntityDao extends BaseJdbcEntityDao {

    public OracleJdbcEntityDao(Connection connection) {
        super(connection);
    }
}
