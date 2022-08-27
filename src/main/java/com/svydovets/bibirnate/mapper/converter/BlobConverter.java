package com.svydovets.bibirnate.mapper.converter;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.SQLException;

import com.svydovets.bibirnate.exceptions.EntityMappingException;

public class BlobConverter implements SqlDataTypeConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convert(Object value, Field field) {
        try {
            var val = (Blob) value;
            var len = val.length();
            return val.getBytes(1, (int) len);
        } catch (SQLException ex) {
            throw new EntityMappingException("Error reading Blob from database", ex);
        }
    }
}
