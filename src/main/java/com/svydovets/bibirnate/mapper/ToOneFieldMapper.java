package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;

import com.svydovets.bibirnate.annotation.JoinColumn;
import com.svydovets.bibirnate.exceptions.EntityMappingException;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ToOneFieldMapper implements EntityFieldMapper {

    private final JdbcEntityDao entityDao;

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public <T> void mapField(Field field, T entity, Object foreignKey) {
        if (!field.isAnnotationPresent(JoinColumn.class)) {
            throw new EntityMappingException("ToOne association should be marked with join column annotation");
        }

        var toOneEntity = entityDao.findById(foreignKey, field.getType()).orElse(null);
        log.trace("related entity {} was loaded for entity {}", toOneEntity, entity);
        field.setAccessible(true);
        field.set(entity, toOneEntity);
    }
}
