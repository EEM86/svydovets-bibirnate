package com.svydovets.bibirnate.jdbc;

import java.lang.reflect.Field;
import java.util.Optional;

public interface JdbcEntityDao {

    <T> Optional<T> findById(Object id, Class<T> type);

    <T> Optional<T> findBy(Field field, Object value, Class<T> type);

    void remove(Object entity);

}
