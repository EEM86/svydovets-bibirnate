package com.svydovets.bibirnate.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationProperties {
    /**
     * Specifies the configuration parameters for database connection.
     */
    DatabaseProperties database;

    /**
     * Specifies the configuration parameters for connection pool.
     */
    ConnectionPoolProperties connectionPool;
}
