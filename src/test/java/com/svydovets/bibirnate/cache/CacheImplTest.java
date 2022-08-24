package com.svydovets.bibirnate.cache;

import com.svydovets.bibirnate.cache.command.extractor.impl.EntityKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.QueryKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.invalidation.impl.EntityKeyInvalidationCommand;
import com.svydovets.bibirnate.entity.TestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.svydovets.bibirnate.cache.key.factory.KeyParamFactory.generateKeyParam;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CacheImplTest {

    private static final Long ONE = 1L;
    private static final Long TWO = 2L;
    private static final String CACHE_MAP = "cacheMap";
    private static Cache cache;
    private static TestEntity entity;

    @BeforeEach
    void beforeEach() {
        cache = new Cache();
        entity = new TestEntity();
    }

    @Test
    void tt() {
        List<String> strings = List.of("", "1");
        TestEntity testEntity = new TestEntity(11);


        cache.put(generateKeyParam(TestEntity.class, "dd", List.class), strings);
        cache.put(generateKeyParam(TestEntity.class, "dkkdkkd", List.class), strings);
        cache.put(generateKeyParam(TestEntity.class, 11), testEntity);

        Object dd = cache.get(
                        generateKeyParam(TestEntity.class, "dd", List.class),
                        QueryKeyExtractorCommand.class)
                .orElse(null);

        assertEquals(strings, dd);

        Object e = cache.get(
                        generateKeyParam(TestEntity.class, 11),
                        EntityKeyExtractorCommand.class)
                .orElse(null);
        assertEquals(testEntity, e);

        cache.invalidateRelated(generateKeyParam(TestEntity.class, 11),
                EntityKeyExtractorCommand.class,
                EntityKeyInvalidationCommand.class);

        Object e1 = cache.get(
                        generateKeyParam(TestEntity.class, 11),
                        EntityKeyExtractorCommand.class)
                .orElse(null);

        assertNull(e1);
    }

}
