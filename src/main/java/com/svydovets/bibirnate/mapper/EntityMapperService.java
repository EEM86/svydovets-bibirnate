package com.svydovets.bibirnate.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.svydovets.bibirnate.annotation.Transient;
import com.svydovets.bibirnate.exceptions.DefaultConstructorNotFoundException;
import com.svydovets.bibirnate.exceptions.EntityMappingException;
import com.svydovets.bibirnate.utils.EntityUtils;

/**
 * Class responsible for converting SQL result set to appropriate java Object structure
 * and converting java Object to appropriate SQL structure.
 */
public class EntityMapperService {

    /**
     * Converts SQL result set to java Object.
     *
     * @param toClass   - object's class structure the raw result set should be converted to.
     * @param resultSet - sql result set
     * @return T - an instance of Entity populated with values from sql result set
     * @throws DefaultConstructorNotFoundException in case no default constructor present in the result Entity class
     * @throws EntityMappingException              in case by some reason an instance of the Entity could not be created
     */
    public static  <T> T mapToObject(Class<T> toClass, ResultSet resultSet) {
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
        } catch (NoSuchMethodException ex) {
            throw new DefaultConstructorNotFoundException(
              String.format("Entity class: %s should contain default empty constructor", toClass.getSimpleName()), ex);
        } catch (InvocationTargetException ex) {
            throw new EntityMappingException(
              String.format("Could create instance of entity: %s", toClass.getSimpleName()), ex);
        } catch (InstantiationException ex) {
            throw new EntityMappingException(
              String.format("Could not create instance of abstract class: %s", toClass.getSimpleName()), ex);
        } catch (IllegalAccessException ex) {
            throw new EntityMappingException(
              String.format("Could not access default constructor for entity: %s", toClass.getSimpleName()), ex);
        } catch (SQLException ex) {
            throw new EntityMappingException("Could not execute sql query", ex);
        }
    }

}
