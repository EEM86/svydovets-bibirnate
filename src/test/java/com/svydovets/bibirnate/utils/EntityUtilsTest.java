package com.svydovets.bibirnate.utils;

import static com.svydovets.bibirnate.utils.CommonConstants.BIG_DECIMAL_FIELD_NAME;
import static com.svydovets.bibirnate.utils.CommonConstants.FLOAT_FIELD_NAME;
import static com.svydovets.bibirnate.utils.CommonConstants.ONE_TO_MANY_FIELD_NAME;
import static com.svydovets.bibirnate.utils.CommonConstants.ONE_TO_ONE_FIELD_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.entities.AllTypesEntity;
import com.svydovets.bibirnate.entities.EntityPrimitives;
import com.svydovets.bibirnate.entities.EntityWithTwoIds;
import com.svydovets.bibirnate.entities.EntityWithoutId;
import com.svydovets.bibirnate.entities.EntityWrongType;
import com.svydovets.bibirnate.exceptions.AmbiguousIdException;
import com.svydovets.bibirnate.exceptions.NoIdException;

import lombok.SneakyThrows;

class EntityUtilsTest {

    @Test
    void isRegularField() throws NoSuchFieldException {
        assertFalse(EntityUtils.isRegularField(EntityWrongType.class.getDeclaredField(ONE_TO_ONE_FIELD_NAME)));
        assertFalse(EntityUtils.isRegularField(EntityWrongType.class.getDeclaredField(ONE_TO_MANY_FIELD_NAME)));
        assertTrue(EntityUtils.isRegularField(AllTypesEntity.class.getDeclaredField(BIG_DECIMAL_FIELD_NAME)));
        assertTrue(EntityUtils.isRegularField(EntityPrimitives.class.getDeclaredField(FLOAT_FIELD_NAME)));
    }

    @Test
    void isEntityField() throws NoSuchFieldException {
        assertTrue(EntityUtils.isEntityField(EntityWrongType.class.getDeclaredField(ONE_TO_ONE_FIELD_NAME)));
        assertFalse(EntityUtils.isEntityField(EntityWrongType.class.getDeclaredField(ONE_TO_MANY_FIELD_NAME)));
        assertFalse(EntityUtils.isEntityField(AllTypesEntity.class.getDeclaredField(BIG_DECIMAL_FIELD_NAME)));
    }

    @Test
    void isEntityCollectionField() throws NoSuchFieldException {
        assertTrue(EntityUtils.isEntityCollectionField(EntityWrongType.class.getDeclaredField(ONE_TO_MANY_FIELD_NAME)));
        assertFalse(EntityUtils.isEntityCollectionField(EntityWrongType.class.getDeclaredField(ONE_TO_ONE_FIELD_NAME)));
        assertFalse(EntityUtils.isEntityField(AllTypesEntity.class.getDeclaredField(BIG_DECIMAL_FIELD_NAME)));
    }

    @ParameterizedTest
    @MethodSource("provideEntityClassesWithFieldNames")
    void getFieldNameWithColumnAnnotation(Class<?> entityClass, String fieldName, String result)
      throws NoSuchFieldException {
        var name = EntityUtils.getColumnName(entityClass.getDeclaredField(fieldName));
        assertEquals(result, name);
    }

    private static Stream<Arguments> provideEntityClassesWithFieldNames() {
        return Stream.of(Arguments.of(AllTypesEntity.class, BIG_DECIMAL_FIELD_NAME, "big_decimal_field"),
          Arguments.of(EntityPrimitives.class, FLOAT_FIELD_NAME, FLOAT_FIELD_NAME));
    }

    @Test
    @SneakyThrows
    void getFieldId_shouldReturnField() {
        var expectedField = EntityPrimitives.class.getDeclaredField("id");
        assertEquals(expectedField, EntityUtils.getIdField(EntityPrimitives.class));
    }

    @Test
    void getFieldId_shouldThrowNoIdException() {
        assertThrows(NoIdException.class, () -> EntityUtils.getIdField(EntityWithoutId.class));
    }

    @Test
    void getFieldId_shouldThrowAmbiguousIdException() {
        assertThrows(AmbiguousIdException.class, () -> EntityUtils.getIdField(EntityWithTwoIds.class));
    }

    @Test
    void getTableName_shouldReturnCustomName() {
        assertEquals("test_table", EntityUtils.getTableName(EntityPrimitives.class));
    }

    @Test
    void getTableName_shouldReturnDefaultName() {
        assertEquals("EntityWithoutId", EntityUtils.getTableName(EntityWithoutId.class));
    }

}