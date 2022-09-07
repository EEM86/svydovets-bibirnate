package com.svydovets.bibirnate.jdbc.impl;

import javax.sql.DataSource;

public class MysqlJdbcEntityDao extends BaseJdbcEntityDao {

    public MysqlJdbcEntityDao(DataSource dataSource) {
        super(dataSource);
    }
}
