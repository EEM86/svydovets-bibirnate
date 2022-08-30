package com.svydovets.bibirnate.cache.key.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;
import com.svydovets.bibirnate.entity.TestEntity;

public class KeyParamFactoryTest {

    private static final Long ONE = 1L;
    private static final String QUERY = "SELECT * FROM products";
    private static final Class<TestEntity> testEntityClass = TestEntity.class;
    private static final Class<List> collectionClass = List.class;
    private static final EntityKeyParam<TestEntity> testEntityKeyParam = new EntityKeyParam<>(testEntityClass, ONE);
    private static final QueryKeyParam<TestEntity> testQueryKeyParam =
      new QueryKeyParam<>(testEntityClass, QUERY, collectionClass);

    @ParameterizedTest
    @MethodSource("provideParamsForNullPointerForEntityKeyParam")
    void generateEntityKeyParam_throwsNullPointerExceptionIfOneFromPassedParametersIsNull(Class<?> entityType,
                                                                                          Object id) {
        assertThrows(NullPointerException.class, () -> KeyParamFactory.generateKeyParam(entityType, id));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCreationEntityKeyParam")
    void generateEntityKeyParam_provideEntityKeyParamIfPassedParametersIsCorrect(Class<?> entityType, Object id) {
        assertEquals(testEntityKeyParam, KeyParamFactory.generateKeyParam(entityType, id));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForNullPointerForQueryKeyParam")
    void generateQueryKeyParam_throwsNullPointerExceptionIfOneFromPassedParametersIsNull(Class<?> entityType,
                                                                                         String query,
                                                                                         Class<? extends Collection> collectionType) {
        assertThrows(NullPointerException.class,
          () -> KeyParamFactory.generateKeyParam(entityType, query, collectionType));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCreationQueryKeyParam")
    void generateQueryKeyParam_provideQueryKeyParamIfPassedParametersIsCorrect(Class<?> entityType, String query,
                                                                               Class<? extends Collection> collectionType) {

        assertEquals(testQueryKeyParam, KeyParamFactory.generateKeyParam(entityType, query, collectionType));
    }

    private static Stream<Arguments> provideParamsForNullPointerForEntityKeyParam() {
        return Stream.of(Arguments.of(null, null), Arguments.of(testEntityClass, null), Arguments.of(null, ONE));
    }

    private static Stream<Arguments> provideParamsForCreationEntityKeyParam() {
        return Stream.of(Arguments.of(testEntityClass, ONE));
    }

    private static Stream<Arguments> provideParamsForNullPointerForQueryKeyParam() {
        return Stream.of(Arguments.of(null, null, null), Arguments.of(testEntityClass, null, null),
          Arguments.of(testEntityClass, QUERY, null), Arguments.of(testEntityClass, null, collectionClass),
          Arguments.of(null, QUERY, collectionClass), Arguments.of(null, null, collectionClass));
    }

    private static Stream<Arguments> provideParamsForCreationQueryKeyParam() {
        return Stream.of(Arguments.of(testEntityClass, QUERY, collectionClass));
    }

}
