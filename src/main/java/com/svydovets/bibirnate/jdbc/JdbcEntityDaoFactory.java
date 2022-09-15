package com.svydovets.bibirnate.jdbc;

import static java.sql.DriverManager.getDriver;

import java.sql.Connection;

import com.svydovets.bibirnate.jdbc.impl.BaseJdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.H2JdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.MysqlJdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.OracleJdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.PostgresJdbcEntityDao;
import com.svydovets.bibirnate.logs.SqlLogger;

import lombok.SneakyThrows;

/**
 * Factory to produce JdbcEntityDao implementation according to a database driver.
 */
public class JdbcEntityDaoFactory {


    /**
     * Create JdcEntityDao from the database connection.
     *
     * @param connection Connection
     * @return JdbcEntityDao instance
     */
    @SneakyThrows
    public static JdbcEntityDao createJdbcEntityDao(Connection connection, SqlLogger sqlLogger) {
        final String url = connection.getMetaData().getURL();
        var driver = getDriver(url);
        var driverName = driver.getClass().getName();

        return switch (driverName) {
            case "org.h2.Driver" -> new H2JdbcEntityDao(connection, sqlLogger);
            case "com.mysql.cj.jdbc.Driver" -> new MysqlJdbcEntityDao(connection, sqlLogger);
            case "org.postgresql.Driver" -> new PostgresJdbcEntityDao(connection, sqlLogger);
            case "oracle.jdbc.driver.OracleDriver" -> new OracleJdbcEntityDao(connection, sqlLogger);
            default ->  new BaseJdbcEntityDao(connection, sqlLogger);
        };
    }

}
