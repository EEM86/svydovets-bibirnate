package com.svydovets.bibirnate.cache.command;

import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.entity.BoboEntity;
import com.svydovets.bibirnate.entity.TestEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class CacheProvider {

    private static final String SELECT = "SELECT ";
    private static final Long ONE = 1L;
    private static final List<TestEntity> TEST_ENTITY_LIST = List.of(new TestEntity(ONE), new TestEntity());
    private static final Set<BoboEntity> BOBO_ENTITY_LIST = Set.of(new BoboEntity(ONE), new BoboEntity());
    private static final List<BiberEntity> BIBER_ENTITY_LIST = List.of(new BiberEntity(ONE), new BiberEntity());

    private CacheProvider() {
    }

    public static Map<Key<?>, Object> provideCacheMap() {
        Map<Key<?>, Object> cacheMap = new ConcurrentHashMap<>(10_000);

        for (int i = 1; i < 1500; i++) {
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(TestEntity.class, i)), new TestEntity(i));
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(BoboEntity.class, i)), new BoboEntity(i));
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(BiberEntity.class, i)), new BiberEntity(i));

            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(TestEntity.class, SELECT + i, List.class)),
                    TEST_ENTITY_LIST);
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(BoboEntity.class, SELECT + i, Set.class)),
                    BOBO_ENTITY_LIST);
            cacheMap.put(new Key<>(KeyParamFactory.generateKeyParam(BiberEntity.class, SELECT + i, List.class)),
                    BIBER_ENTITY_LIST);
        }

        return cacheMap;
    }
}
