package com.svydovets.bibirnate.cache;

import static com.svydovets.bibirnate.cache.command.CacheConstant.SELECT;
import static com.svydovets.bibirnate.cache.key.factory.KeyParamFactory.generateKeyParam;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.svydovets.bibirnate.cache.command.CacheProvider;
import com.svydovets.bibirnate.cache.command.extractor.impl.EntityKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.QueryKeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.entity.BoboEntity;
import com.svydovets.bibirnate.entity.TestEntity;

public class CacheUtilsTest {

    private static final Integer ZERO = 0;
    private static final Integer ONE_HUNDRED = 100;
    private static final int ONE_HUNDRED_THOUSANDS = 100_000;
    private static final AbstractKeyParam<TestEntity> TEST_ENTITY_ABSTRACT_KEY_PARAM =
      generateKeyParam(TestEntity.class, ONE_HUNDRED);
    private static final AbstractKeyParam<BoboEntity> BOBO_ENTITY_ABSTRACT_KEY_PARAM =
      generateKeyParam(BoboEntity.class, ONE_HUNDRED);
    private static final AbstractKeyParam<BiberEntity> BIBER_ENTITY_ABSTRACT_KEY_PARAM =
      generateKeyParam(BiberEntity.class, ONE_HUNDRED);
    private static final AbstractKeyParam<TestEntity> TEST_QUERY_ABSTRACT_KEY_PARAM =
      generateKeyParam(TestEntity.class, SELECT + ONE_HUNDRED, List.class);
    private static final AbstractKeyParam<BoboEntity> BOBO_QUERY_ABSTRACT_KEY_PARAM =
      generateKeyParam(BoboEntity.class, SELECT + ONE_HUNDRED, Set.class);
    private static final AbstractKeyParam<BiberEntity> BIBER_QUERY_ABSTRACT_KEY_PARAM =
      generateKeyParam(BiberEntity.class, SELECT + ONE_HUNDRED, List.class);
    private static final Class<EntityKeyExtractorCommand> ENTITY_EXTRACTOR_COMMAND_CLASS =
      EntityKeyExtractorCommand.class;
    private static final Class<QueryKeyExtractorCommand> QUERY_EXTRACTOR_COMMAND_CLASS = QueryKeyExtractorCommand.class;

    private static Cache secondLevelCache;

    @BeforeAll
    static void beforeAll() {
        secondLevelCache = CacheProvider.provide();
    }

    @ParameterizedTest
    @MethodSource("extract_provideEntity")
    void extract_providesEntityFromSecondLevelCacheableEntity(Class<?> entityType, Object value) {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, true);

        checksForCacheState(cacheContainer);

        assertNotEquals(Optional.empty(), value);
        assertEquals(value, CacheUtils.extract(cacheContainer, entityType, ONE_HUNDRED));
    }

    @Test
    void extract_providesOptionalEmptyWhenExtractFromSecondLevelNotCacheableEntity() {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, true);

        checksForCacheState(cacheContainer);

        assertNotNull(secondLevelCache.get(TEST_ENTITY_ABSTRACT_KEY_PARAM, ENTITY_EXTRACTOR_COMMAND_CLASS).get());
        assertEquals(Optional.empty(), CacheUtils.extract(cacheContainer, TestEntity.class, ONE_HUNDRED));
    }

    @ParameterizedTest
    @MethodSource("extract_provideCollection")
    void extract_providesCollectionFromSecondLevelCacheableEntity(Class<?> entityType,
                                                                  Class<? extends Collection> collectionType,
                                                                  Object value) {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, true);

        checksForCacheState(cacheContainer);

        assertNotEquals(Optional.empty(), value);
        assertEquals(value, CacheUtils.extract(cacheContainer, entityType, SELECT + ONE_HUNDRED, collectionType));
    }

    @Test
    void extract_providesOptionalEmptyCollectionWhenExtractFromSecondLevelNotCacheableEntity() {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, true);

        checksForCacheState(cacheContainer);

        assertNotNull(secondLevelCache.get(TEST_QUERY_ABSTRACT_KEY_PARAM, QUERY_EXTRACTOR_COMMAND_CLASS).get());
        assertEquals(Optional.empty(),
          CacheUtils.extract(cacheContainer, TestEntity.class, SELECT + ONE_HUNDRED, List.class));
    }

    @Test
    void extract_providesOptionalEmptyCollectionWhenSecondLevelCacheDisabled() {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, false);

        checksForCacheState(cacheContainer);

        assertNotNull(secondLevelCache.get(BOBO_QUERY_ABSTRACT_KEY_PARAM, QUERY_EXTRACTOR_COMMAND_CLASS).get());
        assertEquals(Optional.empty(),
          CacheUtils.extract(cacheContainer, BoboEntity.class, SELECT + ONE_HUNDRED, Set.class));
    }

    @Test
    void put_PutToBothLevelCache() {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, true);

        checksForCacheState(cacheContainer);

        BoboEntity value = new BoboEntity(ONE_HUNDRED_THOUSANDS);
        AbstractKeyParam<BoboEntity> boboEntityAbstractKeyParam =
          generateKeyParam(BoboEntity.class, ONE_HUNDRED_THOUSANDS);

        CacheUtils.put(cacheContainer, BoboEntity.class, ONE_HUNDRED_THOUSANDS, value);

        assertEquals(Optional.of(value),
          cacheContainer.getFirstLevelCache().get(boboEntityAbstractKeyParam, ENTITY_EXTRACTOR_COMMAND_CLASS));
        assertEquals(Optional.of(value),
          cacheContainer.getSecondLevelCache().get(boboEntityAbstractKeyParam, ENTITY_EXTRACTOR_COMMAND_CLASS));

        assertEquals(Optional.of(value), CacheUtils.extract(cacheContainer, BoboEntity.class, ONE_HUNDRED_THOUSANDS));
    }

    @Test
    void put_PutToFirstLevelCacheWhenSecondDisabled() {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, false);

        checksForCacheState(cacheContainer);


        BiberEntity value = new BiberEntity(ONE_HUNDRED_THOUSANDS);
        AbstractKeyParam<BiberEntity> biberEntityAbstractKeyParam =
          generateKeyParam(BiberEntity.class, ONE_HUNDRED_THOUSANDS);

        CacheUtils.put(cacheContainer, BiberEntity.class, ONE_HUNDRED_THOUSANDS, value);

        assertEquals(Optional.of(value),
          cacheContainer.getFirstLevelCache().get(biberEntityAbstractKeyParam, ENTITY_EXTRACTOR_COMMAND_CLASS));
        assertEquals(Optional.empty(),
          cacheContainer.getSecondLevelCache().get(biberEntityAbstractKeyParam, ENTITY_EXTRACTOR_COMMAND_CLASS));
        assertEquals(Optional.of(value),
          CacheUtils.extract(cacheContainer, BiberEntity.class, ONE_HUNDRED_THOUSANDS));
    }

    @Test
    void put_PutToFirstLevelCacheWhenSecondEnabledButEntityNotCacheable() {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, true);

        checksForCacheState(cacheContainer);

        TestEntity value = new TestEntity(ONE_HUNDRED_THOUSANDS);
        AbstractKeyParam<TestEntity> testEntityAbstractKeyParam =
          generateKeyParam(TestEntity.class, ONE_HUNDRED_THOUSANDS);

        CacheUtils.put(cacheContainer, TestEntity.class, ONE_HUNDRED_THOUSANDS, value);

        assertEquals(Optional.of(value),
          cacheContainer.getFirstLevelCache().get(testEntityAbstractKeyParam, ENTITY_EXTRACTOR_COMMAND_CLASS));
        assertEquals(Optional.empty(),
          cacheContainer.getSecondLevelCache().get(testEntityAbstractKeyParam, ENTITY_EXTRACTOR_COMMAND_CLASS));
        assertEquals(Optional.of(value),
          CacheUtils.extract(cacheContainer, TestEntity.class, ONE_HUNDRED_THOUSANDS));
    }

    @Test
    void put_PutCollectionToBothLevelCache() {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, true);

        checksForCacheState(cacheContainer);

        Set<BoboEntity> value = Set.of(new BoboEntity(ONE_HUNDRED_THOUSANDS), new BoboEntity());
        AbstractKeyParam<BoboEntity> boboEntityAbstractKeyParam =
          generateKeyParam(BoboEntity.class, SELECT + ONE_HUNDRED_THOUSANDS, Set.class);

        CacheUtils.put(cacheContainer, BoboEntity.class, SELECT + ONE_HUNDRED_THOUSANDS, Set.class, value);

        assertEquals(Optional.of(value),
          cacheContainer.getFirstLevelCache().get(boboEntityAbstractKeyParam, QUERY_EXTRACTOR_COMMAND_CLASS));
        assertEquals(Optional.of(value),
          cacheContainer.getSecondLevelCache().get(boboEntityAbstractKeyParam, QUERY_EXTRACTOR_COMMAND_CLASS));

        assertEquals(Optional.of(value),
          CacheUtils.extract(cacheContainer, BoboEntity.class, SELECT + ONE_HUNDRED_THOUSANDS, Set.class));
    }

    @Test
    void put_PutCollectionToFirstLevelCacheWhenSecondDisabled() {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, false);

        checksForCacheState(cacheContainer);

        List<BiberEntity> value = List.of(new BiberEntity(ONE_HUNDRED_THOUSANDS), new BiberEntity());
        AbstractKeyParam<BiberEntity> biberEntityAbstractKeyParam =
          generateKeyParam(BiberEntity.class, SELECT + ONE_HUNDRED_THOUSANDS, List.class);

        CacheUtils.put(cacheContainer, BiberEntity.class, SELECT + ONE_HUNDRED_THOUSANDS, List.class, value);

        assertEquals(Optional.of(value),
          cacheContainer.getFirstLevelCache().get(biberEntityAbstractKeyParam, QUERY_EXTRACTOR_COMMAND_CLASS));
        assertEquals(Optional.empty(),
          cacheContainer.getSecondLevelCache().get(biberEntityAbstractKeyParam, QUERY_EXTRACTOR_COMMAND_CLASS));

        assertEquals(Optional.of(value),
          CacheUtils.extract(cacheContainer, BiberEntity.class, SELECT + ONE_HUNDRED_THOUSANDS, List.class));
    }

    @Test
    void put_PutCollectionToFirstLevelCacheWhenSecondEnabledButEntityNotCacheable() {
        CacheContainer cacheContainer = new CacheContainer(secondLevelCache, true);

        checksForCacheState(cacheContainer);

        List<TestEntity> value = List.of(new TestEntity(ONE_HUNDRED_THOUSANDS), new TestEntity());
        AbstractKeyParam<TestEntity> testEntityAbstractKeyParam =
          generateKeyParam(TestEntity.class, SELECT + ONE_HUNDRED_THOUSANDS, List.class);

        CacheUtils.put(cacheContainer, TestEntity.class, SELECT + ONE_HUNDRED_THOUSANDS, List.class, value);

        assertEquals(Optional.of(value),
          cacheContainer.getFirstLevelCache().get(testEntityAbstractKeyParam, QUERY_EXTRACTOR_COMMAND_CLASS));
        assertEquals(Optional.empty(),
          cacheContainer.getSecondLevelCache().get(testEntityAbstractKeyParam, QUERY_EXTRACTOR_COMMAND_CLASS));

        assertEquals(Optional.of(value),
          CacheUtils.extract(cacheContainer, TestEntity.class, SELECT + ONE_HUNDRED_THOUSANDS, List.class));
    }

    private static Stream<Arguments> extract_provideEntity() {
        return Stream.of(Arguments.of(BoboEntity.class,
            secondLevelCache.get(BOBO_ENTITY_ABSTRACT_KEY_PARAM, ENTITY_EXTRACTOR_COMMAND_CLASS)),
          Arguments.of(BiberEntity.class,
            secondLevelCache.get(BIBER_ENTITY_ABSTRACT_KEY_PARAM, ENTITY_EXTRACTOR_COMMAND_CLASS))
        );
    }

    private static Stream<Arguments> extract_provideCollection() {
        return Stream.of(Arguments.of(BoboEntity.class, Set.class,
            secondLevelCache.get(BOBO_QUERY_ABSTRACT_KEY_PARAM, QUERY_EXTRACTOR_COMMAND_CLASS)),
          Arguments.of(BiberEntity.class, List.class,
            secondLevelCache.get(BIBER_QUERY_ABSTRACT_KEY_PARAM, QUERY_EXTRACTOR_COMMAND_CLASS))
        );
    }

    private void checksForCacheState(CacheContainer cacheContainer) {
        assertEquals(ZERO, cacheContainer.getFirstLevelCache().size());
        assertTrue(cacheContainer.getSecondLevelCache().size() > ZERO);
    }

}
