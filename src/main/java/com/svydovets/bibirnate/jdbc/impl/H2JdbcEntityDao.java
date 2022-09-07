package com.svydovets.bibirnate.jdbc.impl;

import javax.sql.DataSource;

public class H2JdbcEntityDao extends BaseJdbcEntityDao {

    public H2JdbcEntityDao(DataSource dataSource) {
        super(dataSource);
    }
}
