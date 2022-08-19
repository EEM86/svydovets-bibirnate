package com.svydovets.bibirnate.cache.impl;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.entity.TestEntity;
import com.svydovets.bibirnate.exception.EntityKeyNotPresentException;
import com.svydovets.bibirnate.exception.EntityNotPresentException;
import com.svydovets.bibirnate.exception.EntityTypeNotPresentException;
import com.svydovets.bibirnate.exception.IdNotPresentException;
import com.svydovets.bibirnate.util.EntityKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CacheImplTest {

    private static final Long ONE = 1L;
    private static final Long TWO = 2L;
    private static final String CACHE_MAP = "cacheMap";
    private static Cache cache;
    private static TestEntity entity;

    @BeforeEach
    void beforeEach() {
        cache = new CacheImpl();
        entity = new TestEntity();
    }


    @ParameterizedTest
    @MethodSource("PUT_provideEntityKeyAndEntityForEntityKeyNotPresentException")
    void PUT_shouldThrowEntityKeyNotPresentException_ifEntityKeyOrBothParamsNotPresent(EntityKey<TestEntity> entityKey, TestEntity entity) {
        assertThrows(EntityKeyNotPresentException.class, () -> cache.put(entityKey, entity));
    }

    @ParameterizedTest
    @MethodSource("PUT_DELETE_provideEntityKeyWithoutIdForEntityKeyNotPresentException")
    void PUT_shouldThrowIdNotPresentException_ifIdInEntityKeyNotPresent(EntityKey<TestEntity> entityKey) {
        assertThrows(IdNotPresentException.class, () -> cache.put(entityKey, entity));
    }

    @ParameterizedTest
    @MethodSource("PUT_DELETE_provideEntityKeyWithoutEntityTypeForEntityKeyNotPresentException")
    void PUT_shouldThrowEntityTypeNotPresentException_ifEntityTypeInEntityKeyNotPresent(EntityKey<TestEntity> entityKey) {
        assertThrows(EntityTypeNotPresentException.class, () -> cache.put(entityKey, entity));
    }

    @ParameterizedTest
    @NullSource
    void PUT_shouldThrowEntityNotPresentException_ifEntityNotPresent(TestEntity entity) {
        EntityKey<TestEntity> entityKey = new EntityKey<>(TestEntity.class, ONE);

        assertThrows(EntityNotPresentException.class, () -> cache.put(entityKey, entity));
    }

    @ParameterizedTest
    @MethodSource("PUT_provideEntityKeyAndEntity")
    void PUT_shouldNotInsertIntoCacheParameters_ifCacheIsDisabled(EntityKey<TestEntity> entityKey, TestEntity testEntity) throws NoSuchFieldException, IllegalAccessException {
        cache.disable();
        cache.put(entityKey, testEntity);

        Map<EntityKey<?>, Object> cacheMap = getEntityKeyObjectMap();

        assertNull(cacheMap.get(entityKey));
    }

    @ParameterizedTest
    @MethodSource("PUT_provideEntityKeyAndEntity")
    void PUT_shouldInsertIntoCacheParameters(EntityKey<TestEntity> entityKey, TestEntity testEntity) throws NoSuchFieldException, IllegalAccessException {
        cache.put(entityKey, testEntity);

        Map<EntityKey<?>, Object> cacheMap = getEntityKeyObjectMap();

        assertEquals(testEntity, cacheMap.get(entityKey));
    }

    @ParameterizedTest
    @MethodSource("GET_provideIdAndEntityTypeForIdNotPresentException")
    void GET_shouldThrowIdNotPresentException_ifIdOrBothParamsNotPresent(Object id, Class<TestEntity> type) {
        assertThrows(IdNotPresentException.class, () -> cache.get(id, type));
    }

    @ParameterizedTest
    @NullSource
    void GET_shouldEntityTypeNotPresentException_ifEntityTypeNotPresent(Class<TestEntity> type) {
        assertThrows(EntityTypeNotPresentException.class, () -> cache.get(ONE, type));
    }

    @ParameterizedTest
    @MethodSource("GET_provideIdAndEntityTypeAndEntityKeyAndEntity")
    void GET_shouldReturnOptionalEmpty_ifCacheIsDisabled(Object id, Class<TestEntity> type, EntityKey<TestEntity> entityKey, TestEntity entity) {
        cache.disable();
        cache.put(entityKey, entity);

        assertEquals(Optional.empty(), cache.get(id, type));
    }

    @ParameterizedTest
    @MethodSource("GET_provideIdAndEntityTypeForEmptyResult")
    void GET_shouldReturnOptionalEmpty_ifCacheDoesNotContains(Object id, Class<TestEntity> type) {
        assertEquals(Optional.empty(), cache.get(id, type));
    }

    @ParameterizedTest
    @MethodSource("GET_provideIdAndEntityTypeAndEntityKeyAndEntity")
    void GET_shouldReturnEntityByType(Object id, Class<TestEntity> type, EntityKey<TestEntity> entityKey, TestEntity entity) {
        cache.put(entityKey, entity);

        assertEquals(entity, cache.get(id, type).orElseThrow());
    }

    @ParameterizedTest
    @NullSource
    void DELETE_shouldThrown_ifEntityKeyNotPresent(EntityKey<TestEntity> entityKey) {
        assertThrows(EntityKeyNotPresentException.class, () -> cache.delete(entityKey));
    }

    @ParameterizedTest
    @MethodSource("PUT_DELETE_provideEntityKeyWithoutIdForEntityKeyNotPresentException")
    void DELETE_shouldThrowIdNotPresentException_ifIdInEntityKeyNotPresent(EntityKey<TestEntity> entityKey) {
        assertThrows(IdNotPresentException.class, () -> cache.put(entityKey, entity));
    }

    @ParameterizedTest
    @MethodSource("PUT_DELETE_provideEntityKeyWithoutEntityTypeForEntityKeyNotPresentException")
    void DELETE_shouldThrowEntityTypeNotPresentException_ifEntityTypeInEntityKeyNotPresent(EntityKey<TestEntity> entityKey) {
        assertThrows(EntityTypeNotPresentException.class, () -> cache.put(entityKey, entity));
    }

    @ParameterizedTest
    @MethodSource("PUT_provideEntityKeyAndEntity")
    void DELETE_shouldNotDeleteFromCacheParameters_ifCacheIsDisabled(EntityKey<TestEntity> entityKey, TestEntity entity) {
        cache.put(entityKey, entity);

        if (cache.isEnabled()) {
            cache.disable();
        }

        cache.delete(entityKey);

        if (!cache.isEnabled()) {
            cache.enable();
        }

        assertEquals(entity, cache.get(entityKey.getId(), entityKey.getEntityType()).orElseThrow());
    }

    @ParameterizedTest
    @MethodSource("PUT_provideEntityKeyAndEntity")
    void DELETE_shouldDeleteFromCacheParameters(EntityKey<TestEntity> entityKey, TestEntity entity) {
        cache.put(entityKey, entity);

        cache.delete(entityKey);

        assertEquals(Optional.empty(), cache.get(entityKey.getId(), entityKey.getEntityType()));
    }

    @ParameterizedTest
    @MethodSource("GET_provideIdAndEntityTypeAndEntityKeyAndEntity")
    void CLEAR_shouldClearCache(Object id, Class<TestEntity> type, EntityKey<TestEntity> entityKey, TestEntity entity) {
        cache.put(entityKey, entity);

        cache.clear();

        assertEquals(Optional.empty(), cache.get(id, type));
    }

    private static Stream<Arguments> PUT_provideEntityKeyAndEntityForEntityKeyNotPresentException() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, new TestEntity()));
    }

    private static Stream<Arguments> PUT_DELETE_provideEntityKeyWithoutIdForEntityKeyNotPresentException() {
        return Stream.of(Arguments.of(new EntityKey<>(TestEntity.class)));
    }

    private static Stream<Arguments> PUT_DELETE_provideEntityKeyWithoutEntityTypeForEntityKeyNotPresentException() {
        return Stream.of(Arguments.of(new EntityKey<>(ONE)));
    }

    private static Stream<Arguments> PUT_provideEntityKeyAndEntity() {
        return Stream.of(
                Arguments.of(new EntityKey<>(TestEntity.class, ONE), new TestEntity(ONE)),
                Arguments.of(new EntityKey<>(TestEntity.class, TWO), new TestEntity(TWO)));
    }

    private static Stream<Arguments> GET_provideIdAndEntityTypeForIdNotPresentException() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, TestEntity.class));
    }

    private static Stream<Arguments> GET_provideIdAndEntityTypeForEmptyResult() {
        return Stream.of(
                Arguments.of(ONE, TestEntity.class),
                Arguments.of(TWO, TestEntity.class));
    }

    private static Stream<Arguments> GET_provideIdAndEntityTypeAndEntityKeyAndEntity() {
        return Stream.of(
                Arguments.of(ONE, TestEntity.class, new EntityKey<>(TestEntity.class, ONE), new TestEntity(ONE)),
                Arguments.of(TWO, TestEntity.class, new EntityKey<>(TestEntity.class, TWO), new TestEntity(TWO)));
    }


    @SuppressWarnings("unchecked")
    private Map<EntityKey<?>, Object> getEntityKeyObjectMap() throws NoSuchFieldException, IllegalAccessException {
        Field fieldCacheMap = cache.getClass().getDeclaredField(CACHE_MAP);
        fieldCacheMap.setAccessible(true);
        return (Map<EntityKey<?>, Object>) fieldCacheMap.get(cache);
    }

}
