package com.svydovets.bibirnate.cache;

import com.svydovets.bibirnate.cache.command.CacheProvider;
import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.EntityKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.QueryKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.invalidation.InvalidationCommand;
import com.svydovets.bibirnate.cache.command.invalidation.impl.EntityKeyInvalidationCommand;
import com.svydovets.bibirnate.cache.command.invalidation.impl.QueryKeyInvalidationCommand;
import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.entity.BoboEntity;
import com.svydovets.bibirnate.entity.TestEntity;
import com.svydovets.bibirnate.exception.CacheOverloadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.svydovets.bibirnate.cache.command.CacheConstant.BIBER_KEY_PARAM;
import static com.svydovets.bibirnate.cache.command.CacheConstant.BOBO_KEY_PARAM;
import static com.svydovets.bibirnate.cache.command.CacheConstant.SELECT;
import static com.svydovets.bibirnate.cache.command.CacheConstant.TEST_KEY_PARAM;
import static com.svydovets.bibirnate.cache.key.factory.KeyParamFactory.generateKeyParam;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CacheTest {

    private static final Long ZERO = 0L;
    private static final Long ONE = 1L;
    private static final Long TWO = 2L;
    private static final Integer TEST_ENTITY_VERSION = 228;
    private static final Integer BOBO_ENTITY_VERSION = 777;
    private static final Integer BIBER_ENTITY_VERSION = 555;
    private static final String TEST_VALUE = "test value";
    private static final String BOBO_VALUE = "bobo value";
    private static final String BIBER_VALUE = "biber value";
    private static final TestEntity TEST_ENTITY_VALUE = new TestEntity(TEST_ENTITY_VERSION);
    private static final BoboEntity BOBO_ENTITY_VALUE = new BoboEntity(BOBO_ENTITY_VERSION);
    private static final BiberEntity BIBER_ENTITY_VALUE = new BiberEntity(BIBER_ENTITY_VERSION);
    private static final AbstractKeyParam<TestEntity> TEST_ENTITY_KEY_PARAM =
            generateKeyParam(TestEntity.class, TEST_ENTITY_VERSION);
    private static final AbstractKeyParam<BoboEntity> BOBO_ENTITY_KEY_PARAM =
            generateKeyParam(BoboEntity.class, BOBO_ENTITY_VERSION);
    private static final AbstractKeyParam<BiberEntity> BIBER_ENTITY_KEY_PARAM =
            generateKeyParam(BiberEntity.class, BIBER_ENTITY_VERSION);

    private static final AbstractKeyParam<TestEntity> TEST_QUERY_KEY_PARAM =
            generateKeyParam(TestEntity.class, SELECT + TEST_ENTITY_VERSION, List.class);
    private static final AbstractKeyParam<BoboEntity> BOBO_QUERY_ENTITY_KEY_PARAM =
            generateKeyParam(BoboEntity.class, SELECT + BOBO_ENTITY_VERSION, Set.class);
    private static final AbstractKeyParam<BiberEntity> BIBER_QUERY_KEY_PARAM =
            generateKeyParam(BiberEntity.class, SELECT + BIBER_ENTITY_VERSION, List.class);
    private static final List<TestEntity> TEST_ENTITY_LIST = List.of(TEST_ENTITY_VALUE);
    private static final Set<BoboEntity> BOBO_ENTITY_SET = Set.of(BOBO_ENTITY_VALUE);
    private static final List<BiberEntity> BIBER_ENTITY_LIST = List.of(BIBER_ENTITY_VALUE);


    private static Cache cache;

    @BeforeEach
    void beforeEach() {
        cache = new Cache();
    }

    @ParameterizedTest
    @MethodSource("put_provideNullParamsForNullPointer")
    void put_throwsNullPointerInCaseIfSomeOfParametersIsNull(AbstractKeyParam<?> keyParam, Object value) {
        assertThrows(NullPointerException.class, () -> cache.put(keyParam, value));
    }

    @ParameterizedTest
    @MethodSource("put_provideProvideCorrectParams")
    void put_provideCorrectDataForSuccessfulPutOperation(AbstractKeyParam<?> keyParam, Object value,
                                                         Class<? extends KeyExtractorCommand> commandType) {
        //WHEN
        cache.put(keyParam, value);

        //THEN
        assertEquals(Optional.of(value), cache.get(keyParam, commandType));
    }

    @Test
    void put_throwsCacheOverloadExceptionWhenCacheOverloaded() {
        Cache cacheOverloaded = new Cache(51);

        try {
            for (int i = 0; i < cacheOverloaded.size(); i++) {
                cacheOverloaded.put(KeyParamFactory.generateKeyParam(TestEntity.class, i), i);
            }
        } catch (Exception exception) {
            assertEquals(CacheOverloadException.class, exception.getClass());
        }
    }

    @ParameterizedTest
    @MethodSource("get_provideNullParamsForNullPointer")
    void get_throwNullPointerIfSomeOfParameterIsNull(AbstractKeyParam<?> keyParam,
                                                     Class<? extends KeyExtractorCommand> commandType) {
        assertThrows(NullPointerException.class, () -> cache.get(keyParam, commandType));
    }

    @ParameterizedTest
    @MethodSource("get_provideParamsForOptionalEmpty")
    void get_providesOptionalEmptyIfParametersIsCorrect(AbstractKeyParam<?> keyParam,
                                                        Class<? extends KeyExtractorCommand> commandType) {
        assertEquals(Optional.empty(), cache.get(keyParam, commandType));
    }

    @ParameterizedTest
    @MethodSource("get_provideParamsForOptionalOf")
    void get_providesValueIfParametersIsCorrect(AbstractKeyParam<?> keyParam,
                                                Class<? extends KeyExtractorCommand> commandType, Object value) {
        Cache providedCache = CacheProvider.provide();

        assertEquals(Optional.of(value), providedCache.get(keyParam, commandType));
    }

    @ParameterizedTest
    @MethodSource("invalidateRelated_provideNullParamsForNullPointer")
    void invalidateRelated_throwNullPointerExceptionInCaseIfSomeOfParametersIsNull(AbstractKeyParam<?> keyParam,
                                                                        Class<? extends KeyExtractorCommand> extractCommandType,
                                                                        Class<? extends InvalidationCommand> invalidationCommandType) {
        assertThrows(NullPointerException.class, () -> cache.invalidateRelated(keyParam, extractCommandType,
                invalidationCommandType));
    }

    @ParameterizedTest
    @MethodSource("invalidateRelated_provideCorrectParams")
    void invalidateRelated_invalidatesAllRelatedCaches(AbstractKeyParam<?> keyParam,
                                                       Class<? extends KeyExtractorCommand> extractCommandType,
                                                       Class<? extends InvalidationCommand> invalidationCommandType,
                                                       Object value, AbstractKeyParam<?> relatedKeyParam,
                                                       Class<? extends KeyExtractorCommand> relatedExtractCommandType,
                                                       Object relatedValue) {
        Cache providedCache = CacheProvider.provide();

        assertEquals(Optional.of(value), providedCache.get(keyParam, extractCommandType));
        assertEquals(Optional.of(relatedValue), providedCache.get(relatedKeyParam, relatedExtractCommandType));

        providedCache.invalidateRelated(keyParam, extractCommandType, invalidationCommandType);

        assertEquals(Optional.empty(), providedCache.get(keyParam, extractCommandType));
        assertEquals(Optional.empty(), providedCache.get(relatedKeyParam, relatedExtractCommandType));
    }

    @Test
    void clear_makeClearForCache() {
        assertEquals(ZERO, cache.size());
        cache.put(KeyParamFactory.generateKeyParam(TestEntity.class, ONE), new TestEntity());
        cache.put(KeyParamFactory.generateKeyParam(TestEntity.class, TWO), new TestEntity());
        assertEquals(TWO, cache.size());

        cache.clear();
        assertEquals(ZERO, cache.size());
    }

    @Test
    void addKeyExtractorCommand_makeClearForCache() {
        assertThrows(NullPointerException.class, () -> cache.addKeyExtractorCommand(null));
    }

    @Test
    void addCacheInvalidationCommand_makeClearForCache() {
        assertThrows(NullPointerException.class, () -> cache.addCacheInvalidationCommand(null));
    }

    private static Stream<Arguments> put_provideNullParamsForNullPointer() {
        return Stream.of(Arguments.of(null, null));
    }

    private static Stream<Arguments> put_provideProvideCorrectParams() {
        return Stream.of(
                Arguments.of(TEST_KEY_PARAM, TEST_VALUE, EntityKeyExtractorCommand.class),
                Arguments.of(BOBO_KEY_PARAM, BOBO_VALUE, QueryKeyExtractorCommand.class),
                Arguments.of(BIBER_KEY_PARAM, BIBER_VALUE, QueryKeyExtractorCommand.class));
    }

    private static Stream<Arguments> get_provideNullParamsForNullPointer() {
        return Stream.of(Arguments.of(null, null),
                Arguments.of(TEST_KEY_PARAM, null),
                Arguments.of(null, QueryKeyExtractorCommand.class));
    }

    private static Stream<Arguments> get_provideParamsForOptionalEmpty() {
        return Stream.of(Arguments.of(TEST_KEY_PARAM, EntityKeyExtractorCommand.class),
                Arguments.of(BOBO_KEY_PARAM, QueryKeyExtractorCommand.class),
                Arguments.of(BIBER_KEY_PARAM, QueryKeyExtractorCommand.class));
    }

    private static Stream<Arguments> get_provideParamsForOptionalOf() {
        return Stream.of(
                Arguments.of(generateKeyParam(TestEntity.class, TEST_ENTITY_VERSION), EntityKeyExtractorCommand.class,
                        TEST_ENTITY_VALUE),
                Arguments.of(generateKeyParam(BoboEntity.class, BOBO_ENTITY_VERSION), EntityKeyExtractorCommand.class,
                        BOBO_ENTITY_VALUE),
                Arguments.of(generateKeyParam(BiberEntity.class, BIBER_ENTITY_VERSION), EntityKeyExtractorCommand.class,
                        BIBER_ENTITY_VALUE),
                Arguments.of(generateKeyParam(TestEntity.class, SELECT + TEST_ENTITY_VERSION, List.class),
                        QueryKeyExtractorCommand.class, List.of(TEST_ENTITY_VALUE)),
                Arguments.of(generateKeyParam(BoboEntity.class, SELECT + BOBO_ENTITY_VERSION, Set.class),
                        QueryKeyExtractorCommand.class, Set.of(BOBO_ENTITY_VALUE)),
                Arguments.of(generateKeyParam(BiberEntity.class, SELECT + BIBER_ENTITY_VERSION, List.class),
                        QueryKeyExtractorCommand.class, List.of(BIBER_ENTITY_VALUE)));
    }

    private static Stream<Arguments> invalidateRelated_provideNullParamsForNullPointer() {
        return Stream.of(Arguments.of(null, null, null),
                Arguments.of(TEST_KEY_PARAM, null, null),
                Arguments.of(TEST_KEY_PARAM, QueryKeyExtractorCommand.class, null),
                Arguments.of(null, QueryKeyExtractorCommand.class, null),
                Arguments.of(null, QueryKeyExtractorCommand.class, QueryKeyInvalidationCommand.class),
                Arguments.of(TEST_KEY_PARAM, null, QueryKeyInvalidationCommand.class),
                Arguments.of(null, null, QueryKeyInvalidationCommand.class));
    }

    private static Stream<Arguments> invalidateRelated_provideCorrectParams() {
        return Stream.of(Arguments.of(TEST_ENTITY_KEY_PARAM, EntityKeyExtractorCommand.class,
                        EntityKeyInvalidationCommand.class, TEST_ENTITY_VALUE, TEST_QUERY_KEY_PARAM,
                        QueryKeyExtractorCommand.class, TEST_ENTITY_LIST),
                Arguments.of(BOBO_ENTITY_KEY_PARAM, EntityKeyExtractorCommand.class, EntityKeyInvalidationCommand.class,
                        BOBO_ENTITY_VALUE, BOBO_QUERY_ENTITY_KEY_PARAM, QueryKeyExtractorCommand.class, BOBO_ENTITY_SET),
                Arguments.of(BIBER_ENTITY_KEY_PARAM, EntityKeyExtractorCommand.class, EntityKeyInvalidationCommand.class,
                        BIBER_ENTITY_VALUE, BIBER_QUERY_KEY_PARAM, QueryKeyExtractorCommand.class, BIBER_ENTITY_LIST),

                Arguments.of(TEST_QUERY_KEY_PARAM, QueryKeyExtractorCommand.class, QueryKeyInvalidationCommand.class,
                        TEST_ENTITY_LIST, TEST_ENTITY_KEY_PARAM, EntityKeyExtractorCommand.class, TEST_ENTITY_VALUE),
                Arguments.of(BOBO_QUERY_ENTITY_KEY_PARAM, QueryKeyExtractorCommand.class, QueryKeyInvalidationCommand.class,
                        BOBO_ENTITY_SET, BOBO_ENTITY_KEY_PARAM, EntityKeyExtractorCommand.class, BOBO_ENTITY_VALUE),
                Arguments.of(BIBER_QUERY_KEY_PARAM, QueryKeyExtractorCommand.class, QueryKeyInvalidationCommand.class,
                        BIBER_ENTITY_LIST, BIBER_ENTITY_KEY_PARAM, EntityKeyExtractorCommand.class, BIBER_ENTITY_VALUE));
    }

}
