package com.svydovets.bibirnate.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.entities.AllTypesEntity;
import com.svydovets.bibirnate.entities.EntityPrimitives;
import com.svydovets.bibirnate.entities.EntityWrongType;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;

class EntityFieldMapperFactoryTest {

    private final JdbcEntityDao jdbcEntityDao = mock(JdbcEntityDao.class);
    private final EntityFieldMapperFactory entityFieldMapperFactory = new EntityFieldMapperFactory(jdbcEntityDao);

    @ParameterizedTest
    @MethodSource("provideEntityClassesWithField")
    void getFieldMapper(Field field, Class<?> resultClass) {

        var mapper = entityFieldMapperFactory.getFieldMapper(field);
        assertEquals(resultClass, mapper.getClass());
    }

    private static Stream<Arguments> provideEntityClassesWithField() throws NoSuchFieldException {
        return Stream.of(
          Arguments.of(AllTypesEntity.class.getDeclaredField("doubleField"), RegularFieldMapper.class),
          Arguments.of(EntityPrimitives.class.getDeclaredField("shortField"), RegularFieldMapper.class),
          Arguments.of(EntityWrongType.class.getDeclaredField("manyToOneEntity"),
            ToOneFieldMapper.class),
          Arguments.of(EntityWrongType.class.getDeclaredField("oneToOneEntity"), ToOneFieldMapper.class),
          Arguments.of(EntityWrongType.class.getDeclaredField("oneToManyEntities"),
            ToManyFieldMapper.class)
        );
    }

}