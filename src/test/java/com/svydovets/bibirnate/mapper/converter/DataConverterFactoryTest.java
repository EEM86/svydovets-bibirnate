package com.svydovets.bibirnate.mapper.converter;

import static com.svydovets.bibirnate.utils.CommonConstants.BIG_DECIMAL_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.BYTE_ARRAY_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.DOUBLE_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.FLOAT_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.INT_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.LONG_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.STRING_CONSTANT;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DataConverterFactoryTest {

    @ParameterizedTest
    @MethodSource("provideEntityClassesWithFieldName")
    void getConverter(Object dbValue, Class<?> resultClass) {
        var converter = DataConverterFactory.getConverter(dbValue);
        Assertions.assertEquals(resultClass, converter.getClass());
    }

    private static Stream<Arguments> provideEntityClassesWithFieldName() throws SQLException {
        return Stream.of(
          Arguments.of(INT_CONSTANT, IntConverter.class),
          Arguments.of(BIG_DECIMAL_CONSTANT, BigDecimalConverter.class),
          Arguments.of(FLOAT_CONSTANT, FloatConverter.class),
          Arguments.of(DOUBLE_CONSTANT, DoubleConverter.class),
          Arguments.of(new Timestamp(new Date().getTime()), TimestampConverter.class),
          Arguments.of(new SerialBlob(BYTE_ARRAY_CONSTANT), BlobConverter.class),
          Arguments.of(STRING_CONSTANT, StringConverter.class),
          Arguments.of(LONG_CONSTANT, DefaultConverter.class)
        );
    }
}