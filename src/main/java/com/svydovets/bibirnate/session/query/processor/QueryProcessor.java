package com.svydovets.bibirnate.session.query.processor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.OneToOne;
import com.svydovets.bibirnate.annotation.Transient;
import com.svydovets.bibirnate.logs.SqlLogger;
import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.FetchType;
import com.svydovets.bibirnate.session.query.ToManyRelation;
import com.svydovets.bibirnate.session.query.ToOneRelation;
import com.svydovets.bibirnate.session.validation.ValidationService;
import com.svydovets.bibirnate.utils.EntityUtils;

import lombok.Data;
import lombok.SneakyThrows;

@Data
public abstract class QueryProcessor {

    private Connection connection;

    private String tableName;

    private Field id;

    private List<Field> entityFields;

    private List<ToManyRelation> toManyRelations;

    private List<ToOneRelation> toOneRelations;

    private Object persistentObject;

    private Parent parent;

    private ValidationService validationService = new ValidationService();
    protected final SqlLogger sqlLogger;

    protected QueryProcessor(Object entity, Connection connection, SqlLogger sqlLogger) {
        initialize(entity, connection, null);

        this.sqlLogger = sqlLogger;
    }

    protected QueryProcessor(Object entity, Connection connection, Parent parent, SqlLogger sqlLogger) {
        initialize(entity, connection, parent);
        this.sqlLogger = sqlLogger;
    }

    private void initialize(Object entity, Connection connection, Parent parent) {
        validationService.validateEntity(entity);
        persistentObject = entity;
        var entityClass = entity.getClass();
        this.connection = connection;
        this.tableName = EntityUtils.getTableName(entityClass);
        this.id = EntityUtils.getIdField(entityClass);
        this.entityFields = Arrays.stream(entityClass.getDeclaredFields())
          .filter(field -> !field.isAnnotationPresent(Transient.class))
          .toList();
        this.toManyRelations = entityFields.stream()
          .filter(field -> field.isAnnotationPresent(OneToMany.class))
          .map(field -> wrapToToManyRelationObject(entity, field))
          .filter(relation -> !CollectionUtils.isEmpty(relation.getRelatedEntities()))
          .toList();
        this.toOneRelations = entityFields.stream()
          .filter(field -> field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class))
          .map(field -> wrapToToOneRelationObject(entity, field))
          .toList();
        this.parent = parent;
    }

    @SneakyThrows
    private ToManyRelation wrapToToManyRelationObject(Object entity, Field toManyField) {
        toManyField.setAccessible(true);
        var fieldValue = toManyField.get(entity);
        var annotation = toManyField.getAnnotation(OneToMany.class);
        return new ToManyRelation(annotation.fetch(), annotation.cascade(), annotation.mappedBy(),
          (List<Object>) fieldValue, toManyField);
    }

    @SneakyThrows
    private ToOneRelation wrapToToOneRelationObject(Object entity, Field toOneField) {
        toOneField.setAccessible(true);
        var fieldValue = toOneField.get(entity);
        FetchType fetch;
        CascadeType[] cascadeType;
        String mappedBy;
        if (toOneField.getAnnotation(OneToOne.class) != null) {
            fetch = toOneField.getAnnotation(OneToOne.class).fetch();
            cascadeType = toOneField.getAnnotation(OneToOne.class).cascade();
            mappedBy = toOneField.getAnnotation(OneToOne.class).mappedBy();
        } else {
            fetch = toOneField.getAnnotation(ManyToOne.class).fetch();
            cascadeType = toOneField.getAnnotation(ManyToOne.class).cascade();
            mappedBy = null;
        }
        return new ToOneRelation(fetch, cascadeType, fieldValue, toOneField, mappedBy);
    }

    public boolean hasToManyRelations() {
        return CollectionUtils.isNotEmpty(toManyRelations);
    }

    public boolean hasToOneRelations() {
        return CollectionUtils.isNotEmpty(toOneRelations);
    }

    public boolean hasParent() {
        return !Objects.isNull(this.parent);
    }

    public abstract String generateQuery();

    public abstract void execute();
}
