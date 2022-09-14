package com.svydovets.bibirnate.logs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class SqlLoggerTest {

    @Test
    void shouldPrintSql() {
        var os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));
        SqlLogger sqlLogger = new SqlLogger(true);

        sqlLogger.log("DROP DATABASE bibirnate WITH (FORCE);");

        assertTrue(os.toString().contains("DROP DATABASE bibirnate WITH (FORCE);"));

    }

    @Test
    void shouldNotPrintSql() {
        var os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));
        SqlLogger sqlLogger = new SqlLogger(false);

        sqlLogger.log("DROP DATABASE bibirnate WITH (FORCE);");

        assertFalse(os.toString().contains("DROP DATABASE bibirnate WITH (FORCE);"));
    }

}