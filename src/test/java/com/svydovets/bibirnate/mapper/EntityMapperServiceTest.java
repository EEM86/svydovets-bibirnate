package com.svydovets.bibirnate.mapper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.svydovets.bibirnate.entities.AbstractEntity;
import com.svydovets.bibirnate.entities.Entity;
import com.svydovets.bibirnate.entities.ConstructorEntityWithException;
import com.svydovets.bibirnate.entities.EntityNoDefaultConstructor;
import com.svydovets.bibirnate.entities.EntityWrongType;
import com.svydovets.bibirnate.exceptions.EntityMappingException;

class EntityMapperServiceTest {

    private ResultSet rs;
    private EntityMapperService entityMapperService;

    @BeforeEach
    void beforeEach() {
        rs = Mockito.mock(ResultSet.class);
        entityMapperService = new EntityMapperService();
    }

    @Test
    void mapToObjectSuccess() throws SQLException {
        setupMocks(rs);
        var result = entityMapperService.mapToObject(Entity.class, rs);
        Mockito.verify(rs, times(15)).getObject(anyString());
        assertEquals(1234L, result.getId());
        assertEquals(1234, result.getIntField());
        assertEquals(Integer.valueOf(10).shortValue(), result.getShortField());
        assertEquals(14.88f, result.getFloatField());
        assertEquals(111111.22, result.getDoubleField());
        assertEquals(Boolean.TRUE, result.getBooleanField());
        assertEquals('c', result.getCharField());
        assertEquals(new BigDecimal("123444523424.99"), result.getBigDecimalField());
        assertEquals(new BigDecimal("56789456456.00").toBigInteger(), result.getBigIntegerField());
        assertEquals("hello svydovets", result.getStringField());
        assertEquals(Date.class, result.getDateField().getClass());
        assertEquals(Timestamp.class, result.getTimestampField().getClass());
        assertEquals(LocalDate.class, result.getLocalDateField().getClass());
        assertEquals(LocalDateTime.class, result.getLocalDateTimeField().getClass());
        String inputString = "hello svydovets!";
        Charset charset = Charset.forName("ASCII");
        assertArrayEquals(inputString.getBytes(charset), result.getBlobField());
    }

    @ParameterizedTest
    @MethodSource("provideEntityClassesWithExceptionMessages")
    void mapToObjectFail(Class<?> entityClass, String msg) {
        Exception exception = assertThrows(EntityMappingException.class, () -> {
            entityMapperService.mapToObject(entityClass, rs);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(msg));
    }

    @Test
    void mapToObjectFailSqlException() throws SQLException {
        when(rs.getObject(anyString())).thenThrow(new SQLException());
        Exception exception = assertThrows(EntityMappingException.class, () -> {
            entityMapperService.mapToObject(Entity.class, rs);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Could not execute sql query"));
    }

    @Test
    void mapToObjectFailSqlException22() throws SQLException {
        Exception exception = assertThrows(EntityMappingException.class, () -> {
            entityMapperService.mapToObject(EntityWrongType.class, rs);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(String.format("Field type: %s is not supported", Entity.class.getName())));
    }

    private static Stream<Arguments> provideEntityClassesWithExceptionMessages() {
        return Stream.of(
          Arguments.of(AbstractEntity.class,
            String.format("Could not create instance of abstract class: %s", AbstractEntity.class.getSimpleName())),
          Arguments.of(EntityNoDefaultConstructor.class,
            String.format("Entity class: %s should contain default empty constructor",
              EntityNoDefaultConstructor.class.getSimpleName())),
          Arguments.of(ConstructorEntityWithException.class,
            String.format("Could create instance of entity: %s",
              ConstructorEntityWithException.class.getSimpleName())));
    }

    private void setupMocks(ResultSet rs) throws SQLException {

        when(rs.getObject("id")).thenReturn(1234L);
        when(rs.getObject("int_field")).thenReturn(1234);
        when(rs.getObject("short_field")).thenReturn(10);
        when(rs.getObject("float_field")).thenReturn(14.88f);
        when(rs.getObject("double_field")).thenReturn(111111.22);
        when(rs.getObject("boolean_field")).thenReturn(Boolean.TRUE);
        when(rs.getObject("char_field")).thenReturn("c");
        when(rs.getObject("big_decimal_field")).thenReturn(new BigDecimal("123444523424.99"));
        when(rs.getObject("big_integer_field")).thenReturn(new BigDecimal("56789456456.00"));
        when(rs.getObject("string_field")).thenReturn("hello svydovets");
        when(rs.getObject("date_field")).thenReturn(new Date());
        when(rs.getObject("timestamp_field")).thenReturn(new Timestamp(new Date().getTime()));
        when(rs.getObject("local_date_field")).thenReturn(new Timestamp(new Date().getTime()));
        when(rs.getObject("local_date_time_field")).thenReturn(new Timestamp(new Date().getTime()));
        String inputString = "hello svydovets!";
        Charset charset = Charset.forName("ASCII");
        when(rs.getObject("blob_field")).thenReturn(inputString.getBytes(charset));
    }

}