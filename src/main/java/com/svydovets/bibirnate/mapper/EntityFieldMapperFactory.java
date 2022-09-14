package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;

import com.svydovets.bibirnate.exceptions.EntityMappingException;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.utils.EntityUtils;

/**
 * Factory class to define {@link EntityFieldMapper} implementation.
 */
public class EntityFieldMapperFactory {
    private final EntityFieldMapper regularFieldMapper;

    private final EntityFieldMapper toOneFieldMapper;

    private final EntityFieldMapper toManyFieldMapper;

    public EntityFieldMapperFactory(JdbcEntityDao jdbcEntityDao) {
        this.regularFieldMapper = new RegularFieldMapper();
        this.toOneFieldMapper = new ToOneFieldMapper(jdbcEntityDao);
        this.toManyFieldMapper = new ToManyFieldMapper();
    }

    /**
     * Define which {@link EntityFieldMapper} implementation will be used
     * based on the field's type.
     * Supported implementations:
     * {@link RegularFieldMapper} - converts standard java objects and primitives.
     * {@link ToOneFieldMapper} - converts custom java objects.
     * {@link ToManyFieldMapper} - converts Collections.
     *
     * @return {@link EntityFieldMapper} implementation.
     * @throws EntityMappingException in case the field does not belong to any existing mappers
     *                                or is not properly mapped.
     */
    public EntityFieldMapper getFieldMapper(Field field) {
        if (EntityUtils.isRegularField(field)) {
            return regularFieldMapper;
        } else if (EntityUtils.isEntityField(field)) {
            return toOneFieldMapper;
        } else if (EntityUtils.isEntityCollectionField(field)) {
            return toManyFieldMapper;
        } else {
            throw new EntityMappingException(
              String.format("Field type: %s is not supported", field.getType().getName()));
        }
    }
}
