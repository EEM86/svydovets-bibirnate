package com.svydovets.bibirnate.session.query.processor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.OneToOne;
import com.svydovets.bibirnate.annotation.Transient;
import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.EntityRelation;
import com.svydovets.bibirnate.session.query.FetchType;
import com.svydovets.bibirnate.session.query.ToManyRelation;
import com.svydovets.bibirnate.session.query.ToOneRelation;
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

    public QueryProcessor(Object entity, Connection connection) {
//        todo: validate Entity?
        var entityClass = entity.getClass();
        this.connection = connection;
        this.tableName = EntityUtils.getTableName(entityClass);
        this.id = EntityUtils.getIdField(entityClass);
        this.entityFields = Arrays.stream(entityClass.getFields())
          .filter(field -> field.isAnnotationPresent(Transient.class))
          .toList();
//        todo: handle uniDirectional
        this.toManyRelations = entityFields.stream()
          .filter(field -> field.isAnnotationPresent(OneToMany.class))
          .map(field -> createToManyRelation(entity, field))
          .toList();
//        todo: handle uniDirectional
        this.toOneRelations = entityFields.stream()
          .filter(field -> field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class))
          .map(field -> createToOneRelation(entity, field))
          .toList();

    }

    @SneakyThrows
    private ToManyRelation createToManyRelation(Object entity, Field toManyField) {
        var fieldValue = toManyField.get(entity);
        var annotation = toManyField.getAnnotation(OneToMany.class);
        return new ToManyRelation(annotation.fetch(), annotation.cascade(), (List<Object>) fieldValue);
    }

    @SneakyThrows
    private ToOneRelation createToOneRelation(Object entity, Field toManyField) {
        var fieldValue = toManyField.get(entity);
        FetchType fetch = null;
        CascadeType[] cascadeType = new CascadeType[0];
        try {
            fetch = toManyField.getAnnotation(OneToMany.class).fetch();
            cascadeType = toManyField.getAnnotation(OneToMany.class).cascade();
        } catch (NullPointerException ex) {
            fetch = toManyField.getAnnotation(OneToOne.class).fetch();
            cascadeType = toManyField.getAnnotation(OneToOne.class).cascade();
        }

        return new ToOneRelation(fetch, cascadeType, fieldValue);
    }

//    {
//        return new ToManyRelation(annotation.fetch(), annotation.cascade(), (List<Object>) fieldValue);
//    }

    public boolean hasToManyRelations() {
        return hasRelations(getToManyRelations());
    }

    public boolean hasToOneRelations() {
        return hasRelations(getToOneRelations());
    }

    public abstract CascadeType[] getOperationCascadeType();

    public abstract String generateQuery();

    public abstract void execute();

    private <T extends EntityRelation> boolean hasRelations(List<T> relations) {
        return !relations.isEmpty() &&
          relations.stream()
            .anyMatch(rel -> {
                var cList = Arrays.stream(rel.getCascade()).toList();
                return cList.contains(CascadeType.ALL) || cList.contains(CascadeType.DELETE);
            });
    }
}
