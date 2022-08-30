package com.svydovets.bibirnate.mapper.converter;


import static com.svydovets.bibirnate.mapper.converter.Converter.BIG_DECIMAL;
import static com.svydovets.bibirnate.mapper.converter.Converter.BLOB;
import static com.svydovets.bibirnate.mapper.converter.Converter.DEFAULT;
import static com.svydovets.bibirnate.mapper.converter.Converter.DOUBLE;
import static com.svydovets.bibirnate.mapper.converter.Converter.FLOAT;
import static com.svydovets.bibirnate.mapper.converter.Converter.INTEGER;
import static com.svydovets.bibirnate.mapper.converter.Converter.STRING;
import static com.svydovets.bibirnate.mapper.converter.Converter.TIMESTAMP;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class to define {@link SqlDataTypeConverter} implementation.
 */
public class DataConverterFactory {

    private DataConverterFactory() {
    }

    private static final Map<Converter, SqlDataTypeConverter> CONVERTERS = new HashMap<>();

    static {
        CONVERTERS.put(DEFAULT, new DefaultConverter());
        CONVERTERS.put(INTEGER, new IntConverter());
        CONVERTERS.put(BLOB, new BlobConverter());
        CONVERTERS.put(BIG_DECIMAL, new BigDecimalConverter());
        CONVERTERS.put(FLOAT, new FloatConverter());
        CONVERTERS.put(DOUBLE, new DoubleConverter());
        CONVERTERS.put(TIMESTAMP, new TimestampConverter());
        CONVERTERS.put(STRING, new StringConverter());
    }

    /**
     * Define which {@link SqlDataTypeConverter} implementation will be used
     * based on the sql data type.
     * Supported implementations:
     * @param dbValue - value returned from the database
     *
     * @return {@link SqlDataTypeConverter} implementation.
     */
    public static SqlDataTypeConverter getConverter(Object dbValue) {

        if (dbValue instanceof Integer) {
            return CONVERTERS.get(INTEGER);
        }

        if (dbValue instanceof BigDecimal) {
            return CONVERTERS.get(BIG_DECIMAL);
        }

        if (dbValue instanceof Float) {
            return CONVERTERS.get(FLOAT);
        }

        if (dbValue instanceof Double) {
            return CONVERTERS.get(DOUBLE);
        }

        if (dbValue instanceof Timestamp) {
            return CONVERTERS.get(TIMESTAMP);
        }

        if (dbValue instanceof Blob) {
            return CONVERTERS.get(BLOB);
        }

        if (dbValue instanceof String) {
            return CONVERTERS.get(STRING);
        }

        return CONVERTERS.get(DEFAULT);
    }

}
