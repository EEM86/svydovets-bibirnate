package com.svydovets.bibirnate.session.impl;

import javax.sql.DataSource;

public class JdbcEntityDaoFactory {
    public static JdbcEntityDao createJdbcEntityDao(DataSource dataSource) {
        return new JdbcEntityDao(dataSource);
    }
}
