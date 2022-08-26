package com.svydovets.bibirnate.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;

public final class CommonConstants {

    private CommonConstants() {
    }

    public static long LONG_CONSTANT = 1234L;
    public static int INT_CONSTANT = 1234;
    public static int INT_TO_SHORT_CONSTANT = 10;
    public static short SHORT_CONSTANT = Integer.valueOf(INT_TO_SHORT_CONSTANT).shortValue();
    public static float FLOAT_CONSTANT = 16.54f;
    public static double DOUBLE_CONSTANT = 111213.22;
    public static String STRING_TO_CHAR_CONSTANT = "c";
    public static char CHAR_CONSTANT = 'c';
    public static BigDecimal BIG_DECIMAL_CONSTANT = new BigDecimal("123444523424.99");
    public static BigDecimal BIG_DECIMAL_CONSTANT_DB = new BigDecimal("56789456456.00");
    public static BigInteger BIG_INTEGER_CONSTANT = new BigDecimal("56789456456.00").toBigInteger();
    public static String STRING_CONSTANT = "hello Svydovets!";
    public static byte[] BYTE_ARRAY_CONSTANT = STRING_CONSTANT.getBytes(Charset.forName("ASCII"));
}
