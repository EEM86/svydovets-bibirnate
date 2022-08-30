package com.svydovets.bibirnate.cache.command;

import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.entity.BoboEntity;
import com.svydovets.bibirnate.entity.TestEntity;

import java.util.List;

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

}
