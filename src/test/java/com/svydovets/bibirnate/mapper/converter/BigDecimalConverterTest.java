package com.svydovets.bibirnate.mapper.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.entities.AllTypesEntity;
import com.svydovets.bibirnate.utils.CommonConstants;

class BigDecimalConverterTest {

    @ParameterizedTest
    @MethodSource("provideEntityClassesWithFieldName")
    void convert(String fieldName, Class<?> resultClass) throws NoSuchFieldException {
        var converter = new BigDecimalConverter();
        var res = converter.convert(CommonConstants.BIG_DECIMAL_CONSTANT,
          AllTypesEntity.class.getDeclaredField(fieldName));
        assertEquals(resultClass, res.getClass());
    }

    private static Stream<Arguments> provideEntityClassesWithFieldName() {
        return Stream.of(
          Arguments.of("bigIntegerField", BigInteger.class),
          Arguments.of("bigDecimalField", BigDecimal.class)
        );
    }
}