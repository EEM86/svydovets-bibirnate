package com.svydovets.bibirnate.mapper;

import static com.svydovets.bibirnate.utils.CommonConstants.BIG_DECIMAL_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.BIG_DECIMAL_CONSTANT_DB;
import static com.svydovets.bibirnate.utils.CommonConstants.BIG_INTEGER_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.BYTE_ARRAY_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.CHAR_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.DOUBLE_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.FLOAT_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.INT_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.INT_TO_SHORT_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.LONG_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.SHORT_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.STRING_CONSTANT;
import static com.svydovets.bibirnate.utils.CommonConstants.STRING_TO_CHAR_CONSTANT;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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
import com.svydovets.bibirnate.entities.AllTypesEntity;
import com.svydovets.bibirnate.entities.ConstructorEntityWithException;
import com.svydovets.bibirnate.entities.EntityNoDefaultConstructor;
import com.svydovets.bibirnate.entities.EntityPrimitives;
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
        var result = entityMapperService.mapToObject(AllTypesEntity.class, rs);
        Mockito.verify(rs, times(15)).getObject(anyString());
        assertEquals(LONG_CONSTANT, result.getId());
        assertEquals(INT_CONSTANT, result.getIntField());
        assertEquals(SHORT_CONSTANT, result.getShortField());
        assertEquals(FLOAT_CONSTANT, result.getFloatField());
        assertEquals(DOUBLE_CONSTANT, result.getDoubleField());
        assertEquals(Boolean.TRUE, result.getBooleanField());
        assertEquals(CHAR_CONSTANT, result.getCharField());
        assertEquals(BIG_DECIMAL_CONSTANT, result.getBigDecimalField());
        assertEquals(BIG_INTEGER_CONSTANT, result.getBigIntegerField());
        assertEquals(STRING_CONSTANT, result.getStringField());
        assertEquals(Date.class, result.getDateField().getClass());
        assertEquals(Timestamp.class, result.getTimestampField().getClass());
        assertEquals(LocalDate.class, result.getLocalDateField().getClass());
        assertEquals(LocalDateTime.class, result.getLocalDateTimeField().getClass());
        assertArrayEquals(BYTE_ARRAY_CONSTANT, result.getBlobField());
    }

    @Test
    void mapToObjectPrimitives() throws SQLException {
        setupMocks(rs);
        var result = entityMapperService.mapToObject(EntityPrimitives.class, rs);
        Mockito.verify(rs, times(15)).getObject(anyString());
        assertEquals(LONG_CONSTANT, result.getId());
        assertEquals(INT_CONSTANT, result.getIntField());
        assertEquals(SHORT_CONSTANT, result.getShortField());
        assertEquals(FLOAT_CONSTANT, result.getFloat_field());
        assertEquals(DOUBLE_CONSTANT, result.getDoubleField());
        assertEquals(Boolean.TRUE, result.isBoolean_field());
        assertEquals(CHAR_CONSTANT, result.getCharField());
        assertEquals(BIG_DECIMAL_CONSTANT, result.getBigDecimalField());
        assertEquals(BIG_INTEGER_CONSTANT, result.getBigIntegerField());
        assertEquals(STRING_CONSTANT, result.getStringField());
        assertEquals(Date.class, result.getDate_field().getClass());
        assertEquals(Timestamp.class, result.getTimestampField().getClass());
        assertEquals(LocalDate.class, result.getLocalDateField().getClass());
        assertEquals(LocalDateTime.class, result.getLocalDateTimeField().getClass());
        assertArrayEquals(BYTE_ARRAY_CONSTANT, result.getBlobField());
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
            entityMapperService.mapToObject(AllTypesEntity.class, rs);
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
        assertTrue(actualMessage.contains(String.format("Field type: %s is not supported", AllTypesEntity.class.getName())));
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
        Date curDate = new Date();
        Timestamp curTs = new Timestamp(curDate.getTime());
        when(rs.getObject("id")).thenReturn(LONG_CONSTANT);
        when(rs.getObject("int_field")).thenReturn(INT_CONSTANT);
        when(rs.getObject("short_field")).thenReturn(INT_TO_SHORT_CONSTANT);
        when(rs.getObject("float_field")).thenReturn(FLOAT_CONSTANT);
        when(rs.getObject("double_field")).thenReturn(DOUBLE_CONSTANT);
        when(rs.getObject("boolean_field")).thenReturn(Boolean.TRUE);
        when(rs.getObject("char_field")).thenReturn(STRING_TO_CHAR_CONSTANT);
        when(rs.getObject("big_decimal_field")).thenReturn(BIG_DECIMAL_CONSTANT);
        when(rs.getObject("big_integer_field")).thenReturn(BIG_DECIMAL_CONSTANT_DB);
        when(rs.getObject("string_field")).thenReturn(STRING_CONSTANT);
        when(rs.getObject("date_field")).thenReturn(curDate);
        when(rs.getObject("timestamp_field")).thenReturn(curTs);
        when(rs.getObject("local_date_field")).thenReturn(curTs);
        when(rs.getObject("local_date_time_field")).thenReturn(curTs);
        when(rs.getObject("blob_field")).thenReturn(BYTE_ARRAY_CONSTANT);
    }

}