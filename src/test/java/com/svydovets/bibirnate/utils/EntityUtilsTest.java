package com.svydovets.bibirnate.utils;

import static com.svydovets.bibirnate.utils.CommonConstants.BIG_DECIMAL_FIELD_NAME;
import static com.svydovets.bibirnate.utils.CommonConstants.FLOAT_FIELD_NAME;
import static com.svydovets.bibirnate.utils.CommonConstants.ONE_TO_MANY_FIELD_NAME;
import static com.svydovets.bibirnate.utils.CommonConstants.ONE_TO_ONE_FIELD_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.entities.AllTypesEntity;
import com.svydovets.bibirnate.entities.EntityPrimitives;
import com.svydovets.bibirnate.entities.EntityWrongType;

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
        var name = EntityUtils.getFieldName(entityClass.getDeclaredField(fieldName));
        assertEquals(result, name);
    }

    private static Stream<Arguments> provideEntityClassesWithFieldNames() {
        return Stream.of(Arguments.of(AllTypesEntity.class, BIG_DECIMAL_FIELD_NAME, "big_decimal_field"),
          Arguments.of(EntityPrimitives.class, FLOAT_FIELD_NAME, FLOAT_FIELD_NAME));
    }


}