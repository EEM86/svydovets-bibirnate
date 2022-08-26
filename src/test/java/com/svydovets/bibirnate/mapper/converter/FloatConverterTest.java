package com.svydovets.bibirnate.mapper.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.entities.AllTypesEntity;
import com.svydovets.bibirnate.utils.CommonConstants;

class FloatConverterTest {

    @ParameterizedTest
    @MethodSource("provideEntityClassesWithExceptionMessages")
    void convert(String fieldName, Class<?> resultClass) throws NoSuchFieldException {
        var converter = new FloatConverter();
        var res = converter.convert(CommonConstants.FLOAT_CONSTANT,
          AllTypesEntity.class.getDeclaredField(fieldName));
        assertEquals(resultClass, res.getClass());
    }

    private static Stream<Arguments> provideEntityClassesWithExceptionMessages() {
        return Stream.of(
          Arguments.of("floatField", Float.class),
          Arguments.of("bigDecimalField", BigDecimal.class)
        );
    }
}