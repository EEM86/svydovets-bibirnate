package com.svydovets.bibirnate.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DbDriver {
    H2("org.h2.Driver"),
    MYSQL("com.mysql.cj.jdbc.Driver"),
    POSTGRES("org.postgresql.Driver"),
    ORACLE("oracle.jdbc.driver.OracleDriver");

    private String name;

    public static DbDriver fromName(String name) {
        for (var value : DbDriver.values()) {
            if (value.getName().trim().equalsIgnoreCase(name.trim())) {
                return value;
            }
        }
        return null;
    }

}
