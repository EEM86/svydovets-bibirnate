package com.svydovets.bibirnate.mapper.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.entities.AllTypesEntity;
import com.svydovets.bibirnate.utils.CommonConstants;

class IntConverterTest {

    @ParameterizedTest
    @MethodSource("provideEntityClassesWithFieldName")
    void convert(String fieldName, Class<?> resultClass) throws NoSuchFieldException {
        var converter = new IntConverter();
        var res = converter.convert(CommonConstants.INT_CONSTANT,
          AllTypesEntity.class.getDeclaredField(fieldName));
        assertEquals(resultClass, res.getClass());    }

    private static Stream<Arguments> provideEntityClassesWithFieldName() {
        return Stream.of(
          Arguments.of("intField", Integer.class),
          Arguments.of("shortField", Short.class),
          Arguments.of("bigIntegerField", BigInteger.class)
        );
    }
}