package com.svydovets.bibirnate.jdbc;

import static com.svydovets.bibirnate.jdbc.DbDriver.H2;
import static com.svydovets.bibirnate.jdbc.DbDriver.MYSQL;
import static com.svydovets.bibirnate.jdbc.DbDriver.ORACLE;
import static com.svydovets.bibirnate.jdbc.DbDriver.POSTGRES;
import static java.sql.DriverManager.getDriver;

import java.sql.Connection;
import java.util.EnumMap;
import java.util.Map;

import com.svydovets.bibirnate.jdbc.impl.BaseJdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.H2JdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.MysqlJdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.OracleJdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.PostgresJdbcEntityDao;

import lombok.SneakyThrows;

/**
 * Factory to produce JdbcEntityDao implementation according to a database driver.
 */
public class JdbcEntityDaoFactory {

    private static Map<DbDriver, JdbcEntityDao> driverToDao;

    /**
     * Create JdcEntityDao from the database connection.
     *
     * @param connection Connection
     * @return JdbcEntityDao instance
     */
    @SneakyThrows
    public static JdbcEntityDao createJdbcEntityDao(Connection connection) {
        initSupportedDriversMap(connection);
        final String url = connection.getMetaData().getURL();
        var driver = getDriver(url);
        var driverName = driver.getClass().getName();

        return driverToDao.getOrDefault(DbDriver.fromName(driverName), new BaseJdbcEntityDao(connection));
    }

    private static void initSupportedDriversMap(Connection connection) {
        driverToDao = new EnumMap<>(DbDriver.class);
        driverToDao.put(H2, new H2JdbcEntityDao(connection));
        driverToDao.put(MYSQL, new MysqlJdbcEntityDao(connection));
        driverToDao.put(POSTGRES, new PostgresJdbcEntityDao(connection));
        driverToDao.put(ORACLE, new OracleJdbcEntityDao(connection));
    }

}
