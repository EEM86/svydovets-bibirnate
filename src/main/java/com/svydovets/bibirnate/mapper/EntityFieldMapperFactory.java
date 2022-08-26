package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;

import com.svydovets.bibirnate.exceptions.EntityMappingException;
import com.svydovets.bibirnate.utils.EntityUtils;

/**
 * Factory class to define {@link EntityFieldMapper} implementation.
 */
public class EntityFieldMapperFactory {

    private EntityFieldMapperFactory() {
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
    public static EntityFieldMapper getFieldMapper(Field field) {
        if (EntityUtils.isRegularField(field)) {
            return new RegularFieldMapper();
        } else if (EntityUtils.isEntityField(field)) {
            return new ToOneFieldMapper();
        } else if (EntityUtils.isEntityCollectionField(field)) {
            return new ToManyFieldMapper();
        } else {
            throw new EntityMappingException(
              String.format("Field type: %s is not supported", field.getType().getName()));
        }
    }
}
