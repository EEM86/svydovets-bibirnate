package com.svydovets.bibirnate.jdbc.impl;

import javax.sql.DataSource;

public class PostgresJdbcEntityDao extends BaseJdbcEntityDao {

    public PostgresJdbcEntityDao(DataSource dataSource) {
        super(dataSource);
    }
}
