package com.svydovets.bibirnate.jdbc;

import static com.svydovets.bibirnate.jdbc.DbDriver.H2;
import static com.svydovets.bibirnate.jdbc.DbDriver.MYSQL;
import static com.svydovets.bibirnate.jdbc.DbDriver.ORACLE;
import static com.svydovets.bibirnate.jdbc.DbDriver.POSTGRES;
import static java.sql.DriverManager.getDriver;

import java.util.EnumMap;
import java.util.Map;
import javax.sql.DataSource;

import com.svydovets.bibirnate.jdbc.impl.BaseJdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.H2JdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.MysqlJdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.OracleJdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.PostgresJdbcEntityDao;

import lombok.SneakyThrows;

public class JdbcEntityDaoFactory {

    private static Map<DbDriver, JdbcEntityDao> driverToDao;

    @SneakyThrows
    public static JdbcEntityDao createJdbcEntityDao(DataSource dataSource) {
        initSupportedDriversMap(dataSource);
        var driver = getDriver(dataSource.getConnection().getMetaData().getURL());
        var driverName = driver.getClass().getName();

        return driverToDao.getOrDefault(DbDriver.fromName(driverName), new BaseJdbcEntityDao(dataSource));
    }

    private static void initSupportedDriversMap(DataSource dataSource) {
        driverToDao = new EnumMap<>(DbDriver.class);
        driverToDao.put(H2, new H2JdbcEntityDao(dataSource));
        driverToDao.put(MYSQL, new MysqlJdbcEntityDao(dataSource));
        driverToDao.put(POSTGRES, new PostgresJdbcEntityDao(dataSource));
        driverToDao.put(ORACLE, new OracleJdbcEntityDao(dataSource));
    }

}
