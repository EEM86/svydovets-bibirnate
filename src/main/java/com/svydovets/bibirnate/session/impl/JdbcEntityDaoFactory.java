package com.svydovets.bibirnate.session.impl;

import java.sql.Connection;

public class JdbcEntityDaoFactory {
    public static JdbcEntityDao createJdbcEntityDao(Connection connection) {
        return new JdbcEntityDao(connection);
    }
}
