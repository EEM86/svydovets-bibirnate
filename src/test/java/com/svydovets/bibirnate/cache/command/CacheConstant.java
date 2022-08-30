package com.svydovets.bibirnate.cache.command;

import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.entity.BoboEntity;
import com.svydovets.bibirnate.entity.TestEntity;

import java.util.List;
import java.util.Set;

import static com.svydovets.bibirnate.cache.key.factory.KeyParamFactory.generateKeyParam;

public class CacheConstant {
    public static final Long ONE = 1L;
    public static final String QUERY = "SELECT * FROM products";
    public static final String SELECT = "SELECT ";

    public static final AbstractKeyParam<TestEntity> TEST_KEY_PARAM =
            KeyParamFactory.generateKeyParam(TestEntity.class, ONE);

    public static final AbstractKeyParam<BoboEntity> BOBO_KEY_PARAM =
            KeyParamFactory.generateKeyParam(BoboEntity.class, QUERY, List.class);

    public static final AbstractKeyParam<BiberEntity> BIBER_KEY_PARAM =
            KeyParamFactory.generateKeyParam(BiberEntity.class, QUERY, List.class);
    public static final Integer TEST_ENTITY_VERSION = 228;
    public static final Integer BOBO_ENTITY_VERSION = 777;
    public static final Integer BIBER_ENTITY_VERSION = 555;
    public static final String TEST_VALUE = "test value";
    public static final String BOBO_VALUE = "bobo value";
    public static final String BIBER_VALUE = "biber value";

    public static final TestEntity TEST_ENTITY_VALUE = new TestEntity(TEST_ENTITY_VERSION);
    public static final BoboEntity BOBO_ENTITY_VALUE = new BoboEntity(BOBO_ENTITY_VERSION);
    public static final BiberEntity BIBER_ENTITY_VALUE = new BiberEntity(BIBER_ENTITY_VERSION);
    public static final AbstractKeyParam<TestEntity> TEST_ENTITY_KEY_PARAM =
            generateKeyParam(TestEntity.class, TEST_ENTITY_VERSION);
    public static final AbstractKeyParam<BoboEntity> BOBO_ENTITY_KEY_PARAM =
            generateKeyParam(BoboEntity.class, BOBO_ENTITY_VERSION);
    public static final AbstractKeyParam<BiberEntity> BIBER_ENTITY_KEY_PARAM =
            generateKeyParam(BiberEntity.class, BIBER_ENTITY_VERSION);
    public static final AbstractKeyParam<TestEntity> TEST_QUERY_KEY_PARAM =
            generateKeyParam(TestEntity.class, SELECT + TEST_ENTITY_VERSION, List.class);
    public static final AbstractKeyParam<BoboEntity> BOBO_QUERY_ENTITY_KEY_PARAM =
            generateKeyParam(BoboEntity.class, SELECT + BOBO_ENTITY_VERSION, Set.class);
    public static final AbstractKeyParam<BiberEntity> BIBER_QUERY_KEY_PARAM =
            generateKeyParam(BiberEntity.class, SELECT + BIBER_ENTITY_VERSION, List.class);
    public static final List<TestEntity> TEST_ENTITY_LIST = List.of(TEST_ENTITY_VALUE);
    public static final Set<BoboEntity> BOBO_ENTITY_SET = Set.of(BOBO_ENTITY_VALUE);
    public static final List<BiberEntity> BIBER_ENTITY_LIST = List.of(BIBER_ENTITY_VALUE);

}
