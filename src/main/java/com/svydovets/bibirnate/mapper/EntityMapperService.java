package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.svydovets.bibirnate.annotation.Transient;
import com.svydovets.bibirnate.exceptions.DefaultConstructorNotFoundException;
import com.svydovets.bibirnate.exceptions.EntityMappingException;
import com.svydovets.bibirnate.utils.EntityUtils;

public class EntityMapperService {

    public <T> T mapToObject(Class<T> toClass, ResultSet resultSet) {
        try {
            var constructor = toClass.getConstructor();
            var instance = constructor.newInstance();
            for (Field field : toClass.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Transient.class)) {
                    var fieldName = EntityUtils.getFieldName(field);
                    var dbValue = resultSet.getObject(fieldName);
                    var mapper = EntityFieldMapperFactory.getFieldMapper(field);
                    mapper.mapField(field, instance, dbValue);
                }
            }
            return instance;
        } catch (NoSuchMethodException e) {
            throw new DefaultConstructorNotFoundException(
              String.format("Entity class: %s should contain default empty constructor", toClass.getSimpleName()), e);
        } catch (InvocationTargetException e) {
            throw new EntityMappingException(
              String.format("Could create instance of entity: %s", toClass.getSimpleName()), e);
        } catch (InstantiationException e) {
            throw new EntityMappingException(
              String.format("Could not create instance of abstract class: %s", toClass.getSimpleName()), e);
        } catch (IllegalAccessException e) {
            throw new EntityMappingException(
              String.format("Could not access default constructor for entity: %s", toClass.getSimpleName()), e);
        } catch (SQLException e) {
            throw new EntityMappingException("Could not execute sql query", e);
        }
    }

}
