package com.svydovets.bibirnate.cache.command;

import static com.svydovets.bibirnate.cache.command.CacheConstant.ONE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.entity.BoboEntity;
import com.svydovets.bibirnate.entity.TestEntity;

public final class CacheProvider {

    private static final List<TestEntity> TEST_ENTITY_LIST = List.of(new TestEntity(ONE), new TestEntity());
    public static List<Key<TestEntity>> TEST_ENTITY_KEY_LIST = new ArrayList<>();
    private static final Set<BoboEntity> BOBO_ENTITY_SET = Set.of(new BoboEntity(), new BoboEntity(ONE));
    public static List<Key<BoboEntity>> BOBO_ENTITY_KEY_LIST = new ArrayList<>();
    private static final List<BiberEntity> BIBER_ENTITY_LIST = List.of(new BiberEntity(ONE), new BiberEntity());
    public static List<Key<BiberEntity>> BIBER_ENTITY_KEY_LIST = new ArrayList<>();

    private CacheProvider() {
    }

    public static Map<Key<?>, Object> provideCacheMap() {
        Map<Key<?>, Object> cacheMap = new ConcurrentHashMap<>(10_000);

        List<Key<TestEntity>> testEntityKeyList = new ArrayList<>();
        List<Key<BoboEntity>> boboEntityKeyList = new ArrayList<>();
        List<Key<BiberEntity>> biberEntityKeyList = new ArrayList<>();
        for (int i = 2; i < 1500; i++) {
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(TestEntity.class, i)), new TestEntity(i));
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(BoboEntity.class, i)), new BoboEntity(i));
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(BiberEntity.class, i)), new BiberEntity(i));


            Key<TestEntity> testEntityKey = new Key<>(
              KeyParamFactory.generateKeyParam(TestEntity.class, CacheConstant.SELECT + i, List.class));
            testEntityKeyList.add(testEntityKey);
            cacheMap.put(testEntityKey, TEST_ENTITY_LIST);

            Key<BoboEntity> boboEntityKey = new Key<>(
              KeyParamFactory.generateKeyParam(BoboEntity.class, CacheConstant.SELECT + i, Set.class));
            boboEntityKeyList.add(boboEntityKey);
            cacheMap.put(boboEntityKey, BOBO_ENTITY_SET);

            Key<BiberEntity> biberEntityKey = new Key<>(
              KeyParamFactory.generateKeyParam(BiberEntity.class, CacheConstant.SELECT + i, List.class));
            biberEntityKeyList.add(biberEntityKey);
            cacheMap.put(biberEntityKey, BIBER_ENTITY_LIST);
        }

        TEST_ENTITY_KEY_LIST = testEntityKeyList;
        BOBO_ENTITY_KEY_LIST = boboEntityKeyList;
        BIBER_ENTITY_KEY_LIST = biberEntityKeyList;

        return cacheMap;
    }

    public static Cache provide() {
        Cache cache = new Cache(50_000);

        for (int i = 2; i < 5000; i++) {
            cache.put(KeyParamFactory.generateKeyParam(TestEntity.class, i), new TestEntity(i));
            cache.put(KeyParamFactory.generateKeyParam(BoboEntity.class, i), new BoboEntity(i));
            cache.put(KeyParamFactory.generateKeyParam(BiberEntity.class, i), new BiberEntity(i));

            AbstractKeyParam<TestEntity> testEntityAbstractKeyParam =
              KeyParamFactory.generateKeyParam(TestEntity.class, CacheConstant.SELECT + i, List.class);
            cache.put(testEntityAbstractKeyParam, List.of(new TestEntity(i)));

            AbstractKeyParam<BoboEntity> boboEntityAbstractKeyParam =
              KeyParamFactory.generateKeyParam(BoboEntity.class, CacheConstant.SELECT + i, Set.class);
            cache.put(boboEntityAbstractKeyParam, Set.of(new BoboEntity(i)));

            AbstractKeyParam<BiberEntity> biberEntityAbstractKeyParam =
              KeyParamFactory.generateKeyParam(BiberEntity.class, CacheConstant.SELECT + i, List.class);
            cache.put(biberEntityAbstractKeyParam, List.of(new BiberEntity(i)));
        }


        return cache;
    }

}
