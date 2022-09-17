package com.svydovets.bibirnate.session.query.processor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;

import com.svydovets.bibirnate.annotation.JoinColumn;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.OneToOne;
import com.svydovets.bibirnate.exceptions.PersistenceActionNotSupportedException;
import com.svydovets.bibirnate.exceptions.PersistenceException;
import com.svydovets.bibirnate.logs.SqlLogger;
import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.EntityRelation;
import com.svydovets.bibirnate.session.query.FetchType;
import com.svydovets.bibirnate.session.query.ToManyRelation;
import com.svydovets.bibirnate.session.query.ToOneRelation;
import com.svydovets.bibirnate.utils.EntityUtils;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link QueryProcessor} class.
 * Handles DELETE specific logic including different parent-child relations, cascade/non cascade operations.
 * As per current implementation supports only EAGER fetch type.
 */
@Slf4j
@Getter
public class DeleteQueryProcessor extends QueryProcessor {

    private final Map<Integer, Set<String>> depthQueryQueue;

    private final int queryDepth;

    private static final String DELETE_TEMPLATE = "DELETE FROM %s WHERE %s = %s";
    private static final String DELETE_CHILD_NO_CASCADE = "UPDATE %s SET %s = NULL WHERE %s = %s";


    public DeleteQueryProcessor(Object entity, Connection connection, SqlLogger sqlLogger) {
        super(entity, connection, sqlLogger);
        Comparator<Integer> comp = (Integer::compareTo);
        this.depthQueryQueue = new TreeMap<>(comp.reversed());
        this.queryDepth = 0;
    }

    public DeleteQueryProcessor(Object entity, Connection connection, Parent parent,
                                Map<Integer, Set<String>> deepsQueryQueue, int depth, SqlLogger sqlLogger) {
        super(entity, connection, parent, sqlLogger);
        this.depthQueryQueue = deepsQueryQueue;
        this.queryDepth = depth;

    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public String generateQuery() {
        if (!hasParent()) {
            var idField = this.getId();
            idField.setAccessible(true);
            return String.format(DELETE_TEMPLATE, this.getTableName(), idField.getName(),
              idField.get(this.getPersistentObject()));
        } else {
            return String.format(DELETE_TEMPLATE, this.getTableName(), getParent().getColumnName(),
              getParent().getId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        if (hasToManyRelations()) {
            getToManyRelations().forEach(this::handleToManyEntityRelation);
        }
        if (hasToOneRelations()) {
            getToOneRelations().forEach(this::handleToOneEntityRelation);
        }
        String sql = generateQuery();
        getDepthQueryQueue().computeIfAbsent(getQueryDepth(), k -> new HashSet<>()).add(sql);
        if (getQueryDepth() == 0) {
            try (var statement = getConnection().createStatement()) {
                getDepthQueryQueue()
                  .forEach((key, value) -> value.forEach(query -> {
                      try {
                          sqlLogger.log(query);
                          statement.execute(query);
                      } catch (SQLException ex) {
                          throw new PersistenceException("Could not Execute DELETE statement: " + query, ex);
                      }
                  }));
            } catch (SQLException ex) {
                log.trace("Could not delete entity with id = {}", this.getId());
                throw new PersistenceException("Could not Execute DELETE statement", ex);
            }
            log.trace("Successfully deleted entity with id = {}", this.getId());
        }
        log.trace("Successfully deleted entity with id = {}", this.getId());
    }

    private Parent generateParent(Object relation, String mappedBy) {
        getValidationService().validateChildEntity(relation, mappedBy);
        var parentIdColumnName = EntityUtils.getParentIdColumnName(relation.getClass(), mappedBy);
        var parentIdField = getId();
        parentIdField.setAccessible(true);
        Object parentId = null;
        try {
            parentId = EntityUtils.wrapIdValue(parentIdField.get(this.getPersistentObject()));
        } catch (IllegalAccessException ex) {
            throw new PersistenceException(
              String.format("Could not get value from field: %s", parentIdField.getName()));
        }
        return new Parent(parentIdColumnName, parentId, parentIdField);
    }

    private void handleToManyEntityRelation(ToManyRelation relation) {
        if (relation.getFetch() == FetchType.EAGER) {
            if (hasCascadeRelation(relation)) {
                if (!isToManyChildBackwardMapping(relation)) {
                    var newDepth = queryDepth + 1;
                    relation.getRelatedEntities().forEach(rel -> {
                        Parent parent = generateParent(rel, relation.getMappedBy());
                        DeleteQueryProcessor innerProcessor =
                          new DeleteQueryProcessor(rel, getConnection(), parent, getDepthQueryQueue(), newDepth,
                            sqlLogger);
                        innerProcessor.execute();
                    });
                }
            } else {
                var childEntity = relation.getRelatedEntities().get(0);
                generateUpdateQuery(childEntity, relation.getMappedBy());
            }
        } else {
            throw new PersistenceActionNotSupportedException("LAZY is not yet implemented");
        }
    }

    private boolean isOneToOneBackwardParentMapping(ToOneRelation relation) {
        if (Objects.nonNull(relation.getRelatedEntity()) && getQueryDepth() == -1) {
            var mappedBy = relation.getField().getAnnotation(OneToOne.class).mappedBy();
            return Arrays.stream(relation.getRelatedEntity().getClass().getDeclaredFields())
              .anyMatch(field -> field.isAnnotationPresent(OneToOne.class)
                && mappedBy.equals(field.getName()));
        }
        return false;
    }

    private boolean isOneToOneBackwardChildMapping(ToOneRelation relation) {
        if (Objects.nonNull(getParent()) && Objects.nonNull(relation.getRelatedEntity())) {
            var fieldName = relation.getField().getName();
            return Arrays.stream(getParent().getClass().getDeclaredFields())
              .anyMatch(field -> field.isAnnotationPresent(OneToOne.class)
                && fieldName.equals(field.getAnnotation(OneToOne.class).mappedBy()));
        }
        return false;
    }

    private boolean isToManyBackwardMapping(ToOneRelation relation) {
        if (Objects.nonNull(getParent()) && Objects.nonNull(relation.getRelatedEntity())) {
            var fieldName = relation.getField().getName();
            return Arrays.stream(getParent().getClass().getDeclaredFields())
              .anyMatch(field -> field.isAnnotationPresent(OneToMany.class)
                && fieldName.equals(field.getAnnotation(OneToMany.class).mappedBy()));
        }
        return false;
    }

    private boolean isToManyChildBackwardMapping(ToManyRelation relation) {
        if (CollectionUtils.isNotEmpty(relation.getRelatedEntities()) && getQueryDepth() == -1) {
            var mappedBy = relation.getField().getAnnotation(OneToMany.class).mappedBy();
            return Arrays.stream(relation.getRelatedEntities().get(0).getClass().getDeclaredFields())
              .anyMatch(field -> field.isAnnotationPresent(ManyToOne.class)
                && mappedBy.equals(field.getName()));
        }
        return false;
    }


    private void handleToOneEntityRelation(ToOneRelation relation) {
        //      todo: validate
        if (Objects.nonNull(relation.getRelatedEntity())) {
            if (relation.getFetch() == FetchType.EAGER) {
                if (relation.getField().isAnnotationPresent(OneToOne.class)) {
                    executeOneToOne(relation);
                } else {
                    executeManyToOne(relation);
                }
            } else {
                throw new PersistenceActionNotSupportedException("LAZY is not yet implemented for toOne relations");
            }
        }
    }

    private void executeOneToOne(ToOneRelation relation) {
        if (hasCascadeRelation(relation)) {
            if (relation.getField().isAnnotationPresent(JoinColumn.class)) {
                if (!isOneToOneBackwardChildMapping(relation)) {
                    DeleteQueryProcessor innerProcessor =
                      new DeleteQueryProcessor(relation.getRelatedEntity(),
                        getConnection(), null, getDepthQueryQueue(), -1, sqlLogger);
                    innerProcessor.execute();
                }
            } else {
                if (!isOneToOneBackwardParentMapping(relation)) {
                    var newDepth = queryDepth + 1;
                    var parent = generateParent(relation.getRelatedEntity(), relation.getMappedBy());
                    DeleteQueryProcessor innerProcessor =
                      new DeleteQueryProcessor(relation.getRelatedEntity(), getConnection(), parent,
                        getDepthQueryQueue(), newDepth, sqlLogger);
                    innerProcessor.execute();
                }
            }
        } else {
            if (!relation.getField().isAnnotationPresent(JoinColumn.class)) {
                if (hasCascadeRelation(relation)) {
                    var childEntity = relation.getRelatedEntity();
                    generateUpdateQuery(childEntity, relation.getMappedBy());
                }
            }
        }
    }

    private void executeManyToOne(ToOneRelation relation) {
        if (hasCascadeRelation(relation) && !isToManyBackwardMapping(relation)) {
            DeleteQueryProcessor innerProcessor = new DeleteQueryProcessor(relation.getRelatedEntity(),
              getConnection(), null, getDepthQueryQueue(), -1, sqlLogger);
            innerProcessor.execute();
        }
    }

    private <T extends EntityRelation> boolean hasCascadeRelation(T relation) {
        var cascadeList = Arrays.stream(relation.getCascade()).toList();
        return cascadeList.contains(CascadeType.ALL) || cascadeList.contains(CascadeType.DELETE);
    }

    private void generateUpdateQuery(Object childEntity, String mappedBy) {
        Parent parent = generateParent(childEntity, mappedBy);
        var childTable = EntityUtils.getTableName(childEntity.getClass());
        var query = String.format(DELETE_CHILD_NO_CASCADE, childTable, parent.getColumnName(), parent.getColumnName(),
          parent.getId());
        getDepthQueryQueue().computeIfAbsent(getQueryDepth() + 1, k -> new HashSet<>()).add(query);
    }

}
