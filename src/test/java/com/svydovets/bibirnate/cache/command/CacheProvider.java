package com.svydovets.bibirnate.cache.command;

import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.entity.BoboEntity;
import com.svydovets.bibirnate.entity.TestEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.svydovets.bibirnate.cache.command.CacheConstant.ONE;

public final class CacheProvider {

    private static final List<TestEntity> TEST_ENTITY_LIST = List.of(new TestEntity(ONE), new TestEntity());
    public static final List<Key<TestEntity>> TEST_ENTITY_KEY_LIST = new ArrayList<>();
    private static final Set<BoboEntity> BOBO_ENTITY_LIST = Set.of(new BoboEntity(ONE), new BoboEntity());
    public static final List<Key<BoboEntity>> BOBO_ENTITY_KEY_LIST = new ArrayList<>();
    private static final List<BiberEntity> BIBER_ENTITY_LIST = List.of(new BiberEntity(ONE), new BiberEntity());
    public static final List<Key<BiberEntity>> BIBER_ENTITY_KEY_LIST = new ArrayList<>();


    private CacheProvider() {
    }

    public static Map<Key<?>, Object> provideCacheMap() {
        Map<Key<?>, Object> cacheMap = new ConcurrentHashMap<>(10_000);

        for (int i = 1; i < 1500; i++) {
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(TestEntity.class, i)), new TestEntity(i));
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(BoboEntity.class, i)), new BoboEntity(i));
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(BiberEntity.class, i)), new BiberEntity(i));


            Key<TestEntity> testEntityKey = new Key<>(
                    KeyParamFactory.generateKeyParam(TestEntity.class, CacheConstant.SELECT + i, List.class));
            TEST_ENTITY_KEY_LIST.add(testEntityKey);
            cacheMap.put(testEntityKey, TEST_ENTITY_LIST);

            Key<BoboEntity> boboEntityKey = new Key<>(
                    KeyParamFactory.generateKeyParam(BoboEntity.class, CacheConstant.SELECT + i, Set.class));
            BOBO_ENTITY_KEY_LIST.add(boboEntityKey);
            cacheMap.put(boboEntityKey, BOBO_ENTITY_LIST);

            Key<BiberEntity> biberEntityKey = new Key<>(
                    KeyParamFactory.generateKeyParam(BiberEntity.class, CacheConstant.SELECT + i, List.class));
            BIBER_ENTITY_KEY_LIST.add(biberEntityKey);
            cacheMap.put(biberEntityKey, BIBER_ENTITY_LIST);
        }

        return cacheMap;
    }

}
