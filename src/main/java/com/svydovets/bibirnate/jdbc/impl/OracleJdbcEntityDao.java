package com.svydovets.bibirnate.jdbc.impl;

import javax.sql.DataSource;

public class OracleJdbcEntityDao extends BaseJdbcEntityDao {

    public OracleJdbcEntityDao(DataSource dataSource) {
        super(dataSource);
    }
}
