package com.svydovets.bibirnate.session.query.processor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.OneToOne;
import com.svydovets.bibirnate.annotation.Transient;
import com.svydovets.bibirnate.session.Session;
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

//    private Session session;

    private Connection connection;

    private String tableName;

    private Field id;

    private List<Field> entityFields;

    private List<ToManyRelation> toManyRelations;

    private List<ToOneRelation> toOneRelations;

    private Object persistentObject;

    private Object parentId;

    private ValidationService validationService = new ValidationService();

    protected QueryProcessor(Object entity, Connection connection) {
        initialize(entity, connection, null);
    }

    protected QueryProcessor(Object entity, Connection connection, Field parentId) {
        initialize(entity, connection, parentId);
    }

    private void initialize(Object entity, Connection connection, Field parentId) {
        validationService.validateEntity(entity);
        persistentObject = entity;
        var entityClass = entity.getClass();
        this.connection = connection;
        this.tableName = EntityUtils.getTableName(entityClass);
        this.id = EntityUtils.getIdField(entityClass);
        this.entityFields = Arrays.stream(entityClass.getDeclaredFields())
          .filter(field -> !field.isAnnotationPresent(Transient.class))
          .toList();
//        todo: handle uniDirectional
        this.toManyRelations = entityFields.stream()
          .filter(field -> field.isAnnotationPresent(OneToMany.class))
          .map(field -> wrapToToManyRelationObject(entity, field))
          .toList();
//        todo: handle uniDirectional
        this.toOneRelations = entityFields.stream()
          .filter(field -> field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class))
          .map(field -> wrapToToOneRelationObject(entity, field))
          .toList();
        this.parentId = parentId;
    }

    @SneakyThrows
    private ToManyRelation wrapToToManyRelationObject(Object entity, Field toManyField) {
        var fieldValue = toManyField.get(entity);
        var annotation = toManyField.getAnnotation(OneToMany.class);
        return new ToManyRelation(annotation.fetch(), annotation.cascade(), (List<Object>) fieldValue);
    }

    @SneakyThrows
    private ToOneRelation wrapToToOneRelationObject(Object entity, Field toManyField) {
        var fieldValue = toManyField.get(entity);
        FetchType fetch;
        CascadeType[] cascadeType;
        try {
            fetch = toManyField.getAnnotation(OneToMany.class).fetch();
            cascadeType = toManyField.getAnnotation(OneToMany.class).cascade();
        } catch (NullPointerException ex) {
            fetch = toManyField.getAnnotation(OneToOne.class).fetch();
            cascadeType = toManyField.getAnnotation(OneToOne.class).cascade();
        }
        return new ToOneRelation(fetch, cascadeType, fieldValue);
    }

    public boolean hasToManyRelations() {
        return !toManyRelations.isEmpty();
    }

    public boolean hasToOneRelations() {
        return !toOneRelations.isEmpty();
    }

    public boolean hasParent() {
        return !Objects.isNull(this.parentId);
    }

    public abstract String generateQuery();

    public abstract void execute();
}
