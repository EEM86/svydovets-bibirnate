package com.svydovets.bibirnate.mapper;

import com.svydovets.bibirnate.annotation.JoinColumn;
import com.svydovets.bibirnate.exceptions.EntityMappingException;
import com.svydovets.bibirnate.session.impl.JdbcEntityDao;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class ToOneFieldMapper implements EntityFieldMapper {

    private final JdbcEntityDao entityDao;

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public <T> void mapField(Field field, T entity, Object foreignKey) {
        //        todo: possible solution
        //        todo: resolve assosiated entity type
        //        todo: find assosiated entity by id
        //        todo: field.setAccessible(true);
        //        todo: field.set(entity, innerObject);
        if (!field.isAnnotationPresent(JoinColumn.class)) {
            throw new EntityMappingException("ToOne association should be marked with join column annotation");
        }

        var toOneEntity = entityDao.findById(foreignKey, field.getType()).orElse(null);
        field.setAccessible(true);
        field.set(entity, toOneEntity);
    }
}
