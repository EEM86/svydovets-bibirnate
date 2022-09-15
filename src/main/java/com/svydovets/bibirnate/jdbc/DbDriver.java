package com.svydovets.bibirnate.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum to represent database drivers.
 */
@Getter
@AllArgsConstructor
public enum DbDriver {
    H2("org.h2.Driver"),
    MYSQL("com.mysql.cj.jdbc.Driver"),
    POSTGRES("org.postgresql.Driver"),
    ORACLE("oracle.jdbc.driver.OracleDriver");

    private final String name;

    /**
     * Get enum from the name.
     *
     * @param name String
     * @return DbDriver enum
     */
    public static DbDriver fromName(String name) {
        for (var value : DbDriver.values()) {
            if (value.getName().trim().equalsIgnoreCase(name.trim())) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
