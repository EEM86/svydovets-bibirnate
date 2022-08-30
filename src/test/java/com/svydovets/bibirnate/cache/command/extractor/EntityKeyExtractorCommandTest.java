package com.svydovets.bibirnate.cache.command.extractor;

import com.svydovets.bibirnate.cache.command.CacheProvider;
import com.svydovets.bibirnate.cache.command.extractor.impl.EntityKeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.entity.BoboEntity;
import com.svydovets.bibirnate.entity.TestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.svydovets.bibirnate.cache.command.CacheConstant.BIBER_ENTITY_KEY_PARAM;
import static com.svydovets.bibirnate.cache.command.CacheConstant.BOBO_ENTITY_KEY_PARAM;
import static com.svydovets.bibirnate.cache.command.CacheConstant.ONE;
import static com.svydovets.bibirnate.cache.command.CacheConstant.SELECT;
import static com.svydovets.bibirnate.cache.command.CacheConstant.TEST_ENTITY_KEY_PARAM;
import static com.svydovets.bibirnate.cache.command.CacheConstant.TEST_KEY_PARAM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EntityKeyExtractorCommandTest {
    //todo: provide tests
    // for some of test can be used CacheProvider#provideCacheMap()

    private static Map<Key<?>, Object> cacheMap;

    @BeforeEach
    void setUp() {
        cacheMap = CacheProvider.provideCacheMap();
    }

    @ParameterizedTest
    @MethodSource("provideParamsForNullPointer")
    void executeExtract_throwsNullPointerInCaseIfSomeOfParamsIsNull(Map<Key<?>, Object> cacheMap,
                                                                    AbstractKeyParam<?> keyParam) {
        assertThrows(NullPointerException.class, () -> new EntityKeyExtractorCommand().executeExtract(cacheMap, keyParam));
    }

    @Test
    void executeExtract_throwsIllegalArgumentExceptionInCaseIfNotAssignable() {
        assertThrows(IllegalArgumentException.class,
                () -> new EntityKeyExtractorCommand().executeExtract(cacheMap,
                        KeyParamFactory.generateKeyParam(TestEntity.class, SELECT, List.class)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForOptionalEmpty")
    void executeExtract_returnsOptionalEmptyIfNotExists(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam) {
        assertEquals(Optional.empty(), new EntityKeyExtractorCommand().executeExtract(cacheMap, keyParam));
    }

    @ParameterizedTest
    @MethodSource("provideParamsExtraction")
    void executeExtract_ExtractsKeyIfExists(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam,
                                            Key<?> expectedKey) {
        assertEquals(Optional.of(expectedKey).get(), new EntityKeyExtractorCommand().executeExtract(cacheMap, keyParam).get());
    }

    private static Stream<Arguments> provideParamsForNullPointer() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(cacheMap, null),
                Arguments.of(null, TEST_KEY_PARAM));
    }

    private static Stream<Arguments> provideParamsForOptionalEmpty() {
        return Stream.of(
                Arguments.of(cacheMap, KeyParamFactory.generateKeyParam(TestEntity.class, ONE)),
                Arguments.of(cacheMap, KeyParamFactory.generateKeyParam(BoboEntity.class, ONE)),
                Arguments.of(cacheMap, KeyParamFactory.generateKeyParam(BiberEntity.class, ONE))
        );
    }

    private static Stream<Arguments> provideParamsExtraction() {
        return Stream.of(
                Arguments.of(cacheMap, TEST_ENTITY_KEY_PARAM, new Key<>(TEST_ENTITY_KEY_PARAM)),
                Arguments.of(cacheMap, BOBO_ENTITY_KEY_PARAM, new Key<>(BOBO_ENTITY_KEY_PARAM)),
                Arguments.of(cacheMap, BIBER_ENTITY_KEY_PARAM, new Key<>(BIBER_ENTITY_KEY_PARAM)));
    }

}
