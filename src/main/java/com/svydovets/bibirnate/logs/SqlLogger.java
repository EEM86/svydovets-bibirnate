package com.svydovets.bibirnate.logs;

import lombok.RequiredArgsConstructor;

/**
 * Provides behaviour for logging (via System.out.println()) passed queries.
 */
@RequiredArgsConstructor
public class SqlLogger {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";
    private final boolean sqlLoggingEnabled;

    /**
     * Log passed sql to console like 'Bibirnate: select * from table_name'.
     *
     * @param sql {@link String} query
     */
    public void log(String sql) {
        if (sqlLoggingEnabled) {
            System.out.println(PURPLE + "Bibirnate: " + RED + sql + RESET);
        }
    }
}
