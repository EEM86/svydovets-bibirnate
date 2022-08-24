package com.svydovets.bibirnate.cache.command.util;

import com.svydovets.bibirnate.cache.command.CacheProvider;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.EntityKeyParam;
import com.svydovets.bibirnate.cache.key.parameters.QueryKeyParam;
import com.svydovets.bibirnate.entity.BoboEntity;
import com.svydovets.bibirnate.entity.TestEntity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.svydovets.bibirnate.cache.command.CacheConstant.ONE;
import static com.svydovets.bibirnate.cache.command.CacheConstant.QUERY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandUtilTest {

    private static final AbstractKeyParam<TestEntity> TEST_KEY_PARAM =
            KeyParamFactory.generateKeyParam(TestEntity.class, ONE);
    private static final AbstractKeyParam<BoboEntity> BOBO_KEY_PARAM =
            KeyParamFactory.generateKeyParam(BoboEntity.class, QUERY, List.class);

    @ParameterizedTest
    @MethodSource("provideParamsForNullPointerForMapAndAbstractKeyParam")
    void checkPassedParametersOnNull_throwNullPointerExceptionIfSomeOfPassedParametersNull(Map<Key<?>, Object> cacheMap,
                                                                                           AbstractKeyParam<?> keyParam) {
        assertThrows(NullPointerException.class, () -> CommandUtil.checkPassedParametersOnNull(cacheMap, keyParam));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForNullPointerForMapAndKey")
    void checkPassedParametersOnNull_throwNullPointerExceptionIfSomeOfPassedParametersNull(Map<Key<?>, Object> cacheMap,
                                                                                           Key<?> key) {
        assertThrows(NullPointerException.class, () -> CommandUtil.checkPassedParametersOnNull(cacheMap, key));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForNotAssignable")
    void checkOnIsAssignableTo_throwIllegalArgumentExceptionWhenNotAssignable(AbstractKeyParam<?> keyParam,
                                                                              Class<? extends AbstractKeyParam>
                                                                                      keyParamType) {
        assertThrows(IllegalArgumentException.class, () -> CommandUtil.checkOnIsAssignableTo(keyParam, keyParamType));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForIsAssignable")
    void checkOnIsAssignableTo_returnCastedWhenIsAssignable(AbstractKeyParam<?> keyParamResult,
                                                            AbstractKeyParam<?> keyParam,
                                                            Class<? extends AbstractKeyParam> keyParamType) {
        assertEquals(keyParamResult, CommandUtil.checkOnIsAssignableTo(keyParam, keyParamType));
    }

    @ParameterizedTest
    @MethodSource("provideCacheMapAndEntityType")
    void removeAllCacheWithQueryKeyRelated_RemoveRelatedCaches(Map<Key<?>, Object> cacheMap, Class<?> entityType) {
        //WHEN
        CommandUtil.removeAllCacheWithQueryKeyRelated(cacheMap, entityType);

        //THEN
        for (int i = 1; i < 1200; i++) {
            assertNull(cacheMap.get(CacheProvider.TEST_ENTITY_KEY_LIST.get(i)));
            assertNotNull(cacheMap.get(CacheProvider.BOBO_ENTITY_KEY_LIST.get(i)));
            assertNotNull(cacheMap.get(CacheProvider.BIBER_ENTITY_KEY_LIST.get(i)));
        }
    }

    private static Stream<Arguments> provideParamsForNullPointerForMapAndAbstractKeyParam() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(new ConcurrentHashMap<>(), null),
                Arguments.of(null, TEST_KEY_PARAM));
    }

    private static Stream<Arguments> provideParamsForNullPointerForMapAndKey() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(new ConcurrentHashMap<>(), null),
                Arguments.of(null, new Key<>(TEST_KEY_PARAM)));
    }

    private static Stream<Arguments> provideParamsForNotAssignable() {
        return Stream.of(
                Arguments.of(TEST_KEY_PARAM, QueryKeyParam.class),
                Arguments.of(BOBO_KEY_PARAM, EntityKeyParam.class));
    }

    private static Stream<Arguments> provideParamsForIsAssignable() {
        return Stream.of(
                Arguments.of(BOBO_KEY_PARAM, BOBO_KEY_PARAM, QueryKeyParam.class),
                Arguments.of(TEST_KEY_PARAM, TEST_KEY_PARAM, EntityKeyParam.class));
    }

    private static Stream<Arguments> provideCacheMapAndEntityType() {
        return Stream.of(
                Arguments.of(CacheProvider.provideCacheMap(), TestEntity.class));
    }

}
