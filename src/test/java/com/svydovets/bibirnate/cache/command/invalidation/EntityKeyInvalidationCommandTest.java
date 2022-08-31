package com.svydovets.bibirnate.cache.command.invalidation;

import static com.svydovets.bibirnate.cache.command.CacheConstant.BIBER_ENTITY_KEY;
import static com.svydovets.bibirnate.cache.command.CacheConstant.BIBER_QUERY_KEY;
import static com.svydovets.bibirnate.cache.command.CacheConstant.BOBO_ENTITY_KEY;
import static com.svydovets.bibirnate.cache.command.CacheConstant.BOBO_QUERY_KEY;
import static com.svydovets.bibirnate.cache.command.CacheConstant.SELECT;
import static com.svydovets.bibirnate.cache.command.CacheConstant.TEST_ENTITY_KEY;
import static com.svydovets.bibirnate.cache.command.CacheConstant.TEST_KEY_PARAM;
import static com.svydovets.bibirnate.cache.command.CacheConstant.TEST_QUERY_KEY;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.cache.command.CacheProvider;
import com.svydovets.bibirnate.cache.command.invalidation.impl.EntityKeyInvalidationCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.entity.TestEntity;

public class EntityKeyInvalidationCommandTest {

    private static Map<Key<?>, Object> cacheMap;

    @BeforeEach
    void setUp() {
        cacheMap = CacheProvider.provideCacheMap();
    }

    @ParameterizedTest
    @MethodSource("provideParamsForNullPointer")
    void executeInvalidate_throwsNullPointerInCaseIfSomeOfParamsIsNull(Map<Key<?>, Object> cacheMap,
                                                                       Key<?> key) {
        assertThrows(NullPointerException.class,
          () -> new EntityKeyInvalidationCommand().executeInvalidate(cacheMap, key));
    }

    @Test
    void executeInvalidate_throwsIllegalArgumentExceptionInCaseIfNotAssignable() {
        assertThrows(IllegalArgumentException.class,
          () -> new EntityKeyInvalidationCommand().executeInvalidate(cacheMap,
            new Key<>(KeyParamFactory.generateKeyParam(TestEntity.class, SELECT, List.class))));
    }


    @ParameterizedTest
    @MethodSource("provideParamsForInvalidation")
    void executeInvalidate_ExtractsKeyIfExists(Key<?> key, Key<?> relatedKey, Key<?> notRelatedKey) {
        assertNotNull(cacheMap.get(key));
        assertNotNull(cacheMap.get(relatedKey));
        assertNotNull(cacheMap.get(notRelatedKey));

        new EntityKeyInvalidationCommand().executeInvalidate(cacheMap, key);

        assertNull(cacheMap.get(key));
        assertNull(cacheMap.get(relatedKey));
        assertNotNull(cacheMap.get(notRelatedKey));
    }

    private static Stream<Arguments> provideParamsForNullPointer() {
        return Stream.of(
          Arguments.of(null, null),
          Arguments.of(cacheMap, null),
          Arguments.of(null, new Key<>(TEST_KEY_PARAM)));
    }

    private static Stream<Arguments> provideParamsForInvalidation() {
        return Stream.of(
          Arguments.of(TEST_ENTITY_KEY, TEST_QUERY_KEY, BOBO_ENTITY_KEY),
          Arguments.of(BOBO_ENTITY_KEY, BOBO_QUERY_KEY, BIBER_ENTITY_KEY),
          Arguments.of(BIBER_ENTITY_KEY, BIBER_QUERY_KEY, TEST_ENTITY_KEY));
    }

}
