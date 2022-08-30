package com.svydovets.bibirnate.cache.command.extractor;

import static com.svydovets.bibirnate.cache.command.CacheConstant.BIBER_QUERY_KEY_PARAM;
import static com.svydovets.bibirnate.cache.command.CacheConstant.BOBO_KEY_PARAM;
import static com.svydovets.bibirnate.cache.command.CacheConstant.BOBO_QUERY_ENTITY_KEY_PARAM;
import static com.svydovets.bibirnate.cache.command.CacheConstant.ONE;
import static com.svydovets.bibirnate.cache.command.CacheConstant.SELECT;
import static com.svydovets.bibirnate.cache.command.CacheConstant.TEST_QUERY_KEY_PARAM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.cache.command.CacheProvider;
import com.svydovets.bibirnate.cache.command.extractor.impl.QueryKeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.entity.BoboEntity;
import com.svydovets.bibirnate.entity.TestEntity;

public class QueryKeyExtractorCommandTest {

    private static Map<Key<?>, Object> cacheMap;

    @BeforeEach
    void setUp() {
        cacheMap = CacheProvider.provideCacheMap();
    }

    @ParameterizedTest
    @MethodSource("provideParamsForNullPointer")
    void executeExtract_throwsNullPointerInCaseIfSomeOfParamsIsNull(Map<Key<?>, Object> cacheMap,
                                                                    AbstractKeyParam<?> keyParam) {
        assertThrows(NullPointerException.class,
          () -> new QueryKeyExtractorCommand().executeExtract(cacheMap, keyParam));
    }

    @Test
    void executeExtract_throwsIllegalArgumentExceptionInCaseIfNotAssignable() {
        assertThrows(IllegalArgumentException.class,
          () -> new QueryKeyExtractorCommand().executeExtract(cacheMap,
            KeyParamFactory.generateKeyParam(TestEntity.class, ONE)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForOptionalEmpty")
    void executeExtract_returnsOptionalEmptyIfNotExists(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam) {
        assertEquals(Optional.empty(), new QueryKeyExtractorCommand().executeExtract(cacheMap, keyParam));
    }

    @ParameterizedTest
    @MethodSource("provideParamsExtraction")
    void executeExtract_ExtractsKeyIfExists(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam,
                                            Key<?> expectedKey) {
        assertEquals(Optional.of(expectedKey), new QueryKeyExtractorCommand().executeExtract(cacheMap, keyParam));
    }

    private static Stream<Arguments> provideParamsForNullPointer() {
        return Stream.of(
          Arguments.of(null, null),
          Arguments.of(cacheMap, null),
          Arguments.of(null, BOBO_KEY_PARAM));
    }

    private static Stream<Arguments> provideParamsForOptionalEmpty() {
        return Stream.of(
          Arguments.of(cacheMap, KeyParamFactory.generateKeyParam(TestEntity.class, SELECT, Set.class)),
          Arguments.of(cacheMap, KeyParamFactory.generateKeyParam(BoboEntity.class, SELECT, List.class)),
          Arguments.of(cacheMap, KeyParamFactory.generateKeyParam(BiberEntity.class, SELECT, Set.class))
        );
    }

    private static Stream<Arguments> provideParamsExtraction() {
        return Stream.of(
          Arguments.of(cacheMap, TEST_QUERY_KEY_PARAM, new Key<>(TEST_QUERY_KEY_PARAM)),
          Arguments.of(cacheMap, BOBO_QUERY_ENTITY_KEY_PARAM, new Key<>(BOBO_QUERY_ENTITY_KEY_PARAM)),
          Arguments.of(cacheMap, BIBER_QUERY_KEY_PARAM, new Key<>(BIBER_QUERY_KEY_PARAM)));
    }

}
