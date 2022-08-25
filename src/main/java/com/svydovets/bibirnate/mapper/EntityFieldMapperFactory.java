package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;

import com.svydovets.bibirnate.exceptions.EntityMappingException;
import com.svydovets.bibirnate.utils.EntityUtils;

public class EntityFieldMapperFactory {

    public static EntityFieldMapper getFieldMapper(Field field) {
        if (EntityUtils.isRegularField(field)) {
            return new RegularFieldMapper();
        } else if (EntityUtils.isEntityField(field)) {
            return new ToOneFieldMapper();
        } else if (EntityUtils.isEntityCollectionField(field)) {
            return new ToManyFieldMapper();
        } else {
            throw new EntityMappingException(String.format("Field type: %s is not supported", field.getType().getName()));
        }
    }
}
