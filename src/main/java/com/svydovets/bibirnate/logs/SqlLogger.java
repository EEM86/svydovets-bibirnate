package com.svydovets.bibirnate.logs;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SqlLogger {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";
    private final boolean sqlLoggingEnabled;

    public void log(String sql) {
        if (sqlLoggingEnabled) {

            System.out.println(PURPLE + "Bibirnate: " + RED + sql + RESET);
        }
    }
}
