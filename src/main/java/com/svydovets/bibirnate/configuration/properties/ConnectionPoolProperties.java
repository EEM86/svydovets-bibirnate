package com.svydovets.bibirnate.configuration.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Specifies additional configuration parameters for connection pool.
 */
public class ConnectionPoolProperties {

    static final String DEFAULT_SCHEMA = "public";
    static final String DEFAULT_POOL_NAME = "BibirnatePool";
    static final String DEFAULT_EXCEPTION_OVERRIDE_CLASS_NAME = "com.zaxxer.hikari.SQLExceptionOverride";
    static final boolean DEFAULT_IS_AUTO_COMMIT = true;
    static final boolean DEFAULT_IS_READ_ONLY = false;
    static final boolean DEFAULT_ISOLATE_INTERNAL_QUERIES = false;
    static final int DEFAULT_MIN_IDLE = -1;
    static final int DEFAULT_MAX_POOL_SIZE = -1;
    static final long DEFAULT_MAX_LIFETIME = 1800000;
    static final long DEFAULT_CONNECTION_TIMEOUT = 30000;
    static final long DEFAULT_VALIDATION_TIMEOUT = 5000;
    static final long DEFAULT_IDLE_TIMEOUT = 600000;
    static final long DEFAULT_LEAK_DETECTION_THRESHOLD = 0;
    static final long DEFAULT_FAILURE_TIMEOUT = 1;
    static final long DEFAULT_KEEPALIVE_TIME = 300000;

    /**
     * The default catalog name to be set on connections.
     */
    String catalog;
    /**
     * The maximum number of milliseconds that a client will wait for a connection from the pool.
     * If this time is exceeded without a connection becoming available,
     * a SQLException will be thrown from javax.sql.DataSource.getConnection().
     */
    String schema = DEFAULT_SCHEMA;
    /**
     * The name of the connection pool.
     */
    String poolName = DEFAULT_POOL_NAME;
    /**
     * The DataSource class name.
     */
    String dataSourceClassName;
    /**
     * The DataSource JNDI name.
     */
    String dataSourceJndiName;
    /**
     * The SQL string that will be executed on all new connections when they are created,
     * before they are added to the pool.
     */
    String connectionInitSql;
    /**
     * The SQL query to be executed to test the validity of connections.
     */
    String connectionTestQuery;
    /**
     * The name of default transaction isolation level.
     */
    String transactionIsolationName;
    /**
     * The class name of the SQLException subclass that HikariCP should use to override the SQLException
     * thrown by the JDBC driver.
     */
    String exceptionOverrideClassName = DEFAULT_EXCEPTION_OVERRIDE_CLASS_NAME;

    /**
     * Specifies whether connections are auto-commit by default.
     */
    boolean isAutoCommit = DEFAULT_IS_AUTO_COMMIT;
    /**
     * Specifies whether connections are read-only by default.
     */
    boolean isReadOnly = DEFAULT_IS_READ_ONLY;
    /**
     * Determine whether internal pool queries, principally aliveness checks,
     * will be isolated in their own transaction via Connection.rollback().
     */
    boolean isIsolateInternalQueries = DEFAULT_ISOLATE_INTERNAL_QUERIES;
    /**
     * Determine whether HikariCP will self-register HikariConfigMXBean and HikariPoolMXBean instances in JMX.
     */
    boolean isRegisterMbeans;
    /**
     * Specifies  whether pool suspension is allowed.
     * There is a performance impact when pool suspension is enabled.
     * Unless you need it (for a redundancy system for example) do not enable it.
     */
    boolean isAllowPoolSuspension;

    /**
     * The property controls the maximum number of connections that HikariCP will keep in the pool,
     * including both idle and in-use connections.
     */
    int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
    /**
     * The property controls the minimum number of idle connections that HikariCP tries to maintain in the pool,
     * including both idle and in-use connections.
     * If the idle connections dip below this value, HikariCP will make the best effort
     * to restore them quickly and efficiently.
     */
    int minIdle = DEFAULT_MIN_IDLE;
    /**
     * the maximum number of milliseconds that a client will wait for a connection from the pool.
     * If this time is exceeded without a connection becoming available,
     * a SQLException will be thrown from javax.sql.DataSource.getConnection().
     */
    long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    /**
     * The maximum number of milliseconds that the pool will wait for a connection to be validated as alive.
     */
    long validationTimeout = DEFAULT_VALIDATION_TIMEOUT;
    /**
     * This property controls the maximum amount of time (in milliseconds) that a connection
     * is allowed to sit idle in the pool. Whether a connection is retired as idle or not is subject
     * to a maximum variation of +30 seconds, and average variation of +15 seconds.
     * A connection will never be retired as idle before this timeout.
     * A value of 0 means that idle connections are never removed from the pool.
     */
    long idleTimeout = DEFAULT_IDLE_TIMEOUT;
    /**
     * This property controls the amount of time that a connection can be out of the pool before a message
     * is logged indicating a possible connection leak.
     * A value of 0 means leak detection is disabled.
     */
    long leakDetectionThreshold = DEFAULT_LEAK_DETECTION_THRESHOLD;
    /**
     * This property controls the maximum lifetime of a connection in the pool.
     * When a connection reaches this timeout, even if recently used, it will be retired from the pool.
     * An in-use connection will never be retired, only when it is idle will it be removed.
     */
    long maxLifetime = DEFAULT_MAX_LIFETIME;
    /**
     * the pool initialization failure timeout.
     * This setting applies to pool initialization when HikariDataSource is constructed with a HikariConfig,
     * or when HikariDataSource is constructed using the no-arg constructor
     * and HikariDataSource.getConnection() is called.
     */
    long initializationFailTimeout = DEFAULT_FAILURE_TIMEOUT;
    /**
     * This property controls the keepalive interval for a connection in the pool.
     * An in-use connection will never be tested by the keepalive thread,
     * only when it is idle will it be tested.
     */
    long keepaliveTime = DEFAULT_KEEPALIVE_TIME;
}
