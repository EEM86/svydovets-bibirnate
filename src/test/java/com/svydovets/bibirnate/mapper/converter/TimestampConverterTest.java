package com.svydovets.bibirnate.mapper.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.entities.AllTypesEntity;

class TimestampConverterTest {

    @ParameterizedTest
    @MethodSource("provideEntityClassesWithFieldName")
    void convert(String fieldName, Class<?> resultClass) throws NoSuchFieldException {
        var converter = new TimestampConverter();
        var res = converter.convert(new Timestamp(new Date().getTime()),
          AllTypesEntity.class.getDeclaredField(fieldName));
        assertEquals(resultClass, res.getClass());
    }

    private static Stream<Arguments> provideEntityClassesWithFieldName() {
        return Stream.of(
          Arguments.of("localDateField", LocalDate.class),
          Arguments.of("localDateTimeField", LocalDateTime.class),
          Arguments.of("dateField", Date.class),
          Arguments.of("timestampField", Timestamp.class)

        );
    }
}