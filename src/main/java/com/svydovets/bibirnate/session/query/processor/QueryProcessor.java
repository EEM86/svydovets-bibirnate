package com.svydovets.bibirnate.session.query.processor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

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

//    private Map<Integer, Set<String>> depthQueryQueue;
//
//    private Integer queryDepth;

    private ValidationService validationService = new ValidationService();
    protected final SqlLogger sqlLogger;

    protected QueryProcessor(Object entity, Connection connection, SqlLogger sqlLogger) {
//        initialize(entity, connection, null, null);
        initialize(entity, connection, null);
        this.sqlLogger = sqlLogger;
    }

    protected QueryProcessor(Object entity, Connection connection, Parent parent, SqlLogger sqlLogger){
//                             Map<Integer, Set<String>> queryQueue) {
//        initialize(entity, connection, parent, queryQueue);
        initialize(entity, connection, parent);
        this.sqlLogger = sqlLogger;
    }

//    private void initialize(Object entity, Connection connection, Parent parent, Map<Integer, Set<String>> queryQueue) {
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

        //todo: handle uniDirectional
        this.toManyRelations = entityFields.stream()
          .filter(field -> field.isAnnotationPresent(OneToMany.class))
          .map(field -> wrapToToManyRelationObject(entity, field))
          .filter(relation -> !CollectionUtils.isEmpty(relation.getRelatedEntities()))
          .toList();
        //todo: handle uniDirectional
//        this.toOneRelations = entityFields.stream()
//          .filter(field -> field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class))
//          .map(field -> wrapToToOneRelationObject(entity, field))
//          .toList();
        this.parent = parent;
//        if (Objects.isNull(queryQueue)) {
//            this.depthQueryQueue = new HashMap<>();
//            queryDepth = 0;
//        } else {
//            this.depthQueryQueue = queryQueue;
//            this.queryDepth = this.depthQueryQueue.keySet().stream()
//              .max(Integer::compare).orElse(0);
//        }
    }

    @SneakyThrows
    private ToManyRelation wrapToToManyRelationObject(Object entity, Field toManyField) {
        toManyField.setAccessible(true);
        var fieldValue = toManyField.get(entity);
        var annotation = toManyField.getAnnotation(OneToMany.class);
        return new ToManyRelation(annotation.fetch(), annotation.cascade(), annotation.mappedBy(),
          (List<Object>) fieldValue);
    }

    @SneakyThrows
    private ToOneRelation wrapToToOneRelationObject(Object entity, Field toManyField) {
        toManyField.setAccessible(true);
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
        return !Objects.isNull(this.parent);
    }

    public abstract String generateQuery();

    public abstract void execute();
}
