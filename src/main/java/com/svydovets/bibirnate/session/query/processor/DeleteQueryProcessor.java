package com.svydovets.bibirnate.session.query.processor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.svydovets.bibirnate.exceptions.PersistenceException;
import com.svydovets.bibirnate.logs.SqlLogger;
import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.EntityRelation;
import com.svydovets.bibirnate.session.query.FetchType;
import com.svydovets.bibirnate.session.query.ToManyRelation;
import com.svydovets.bibirnate.utils.EntityUtils;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class DeleteQueryProcessor extends QueryProcessor {

    private Map<Integer, Set<String>> depthQueryQueue;

    private int queryDepth;

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

    @Override
    public void execute() {
//        try (var statement = getConnection().createStatement()) {
        if (hasToManyRelations()) {
            getToManyRelations().forEach(this::handleToManyEntityRelation);
            //todo: handle OneToMany biDir + uniDir
//            handleToManyRelations();
        }
//        if (hasToOneRelations()) {
//            //todo: will be handled in relations ticket
//            //          OneToOne biDir + uniDir
//            //          ManyToOne biDir + uniDir
//        }
//        try (var statement = getConnection().createStatement()) {
        String sql = generateQuery();
        sqlLogger.log(sql);
//            getDepthQueryQueue().get(getQueryDepth()).add(sql);
        getDepthQueryQueue().computeIfAbsent(getQueryDepth(), k -> new HashSet<>()).add(sql);
//            statement.execute(sql);
//        } catch (SQLException ex) {
//            throw new PersistenceException("Could not Execute DELETE statement", ex);
//        }
        if (getQueryDepth() == 0) {
            try (var statement = getConnection().createStatement()) {
                getDepthQueryQueue().entrySet().forEach(queriesSet -> {
                    queriesSet.getValue().forEach(query -> {
//                        System.out.println(query);
//                        try {
                            System.out.println(query);
//                            statement.execute(query);
//                        } catch (SQLException ex) {
//                            throw new PersistenceException("Could not Execute DELETE statement: " + query, ex);
//                        }
                    });
                });
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
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return new Parent(parentIdColumnName, parentId, parentIdField);
    }

    @SneakyThrows
    private void handleToManyEntityRelation(ToManyRelation relation) {
        if (relation.getFetch() == FetchType.EAGER) {

            if (hasCascadeRelation(relation)) {
                var newDepth = queryDepth + 1;
                relation.getRelatedEntities().forEach(rel -> {
                    Parent parent = generateParent(rel, relation.getMappedBy());
                    DeleteQueryProcessor innerProcessor =
                      new DeleteQueryProcessor(rel, getConnection(), parent, getDepthQueryQueue(), newDepth, sqlLogger);
                    innerProcessor.execute();
                });


            } else {
                var childEntity = relation.getRelatedEntities().get(0);
//                getValidationService().validateChildEntity(childEntity, relation.getMappedBy());
//
//                var parentIdColumnName =
//                  EntityUtils.getParentIdColumnName(childEntity.getClass(), relation.getMappedBy());
//                var parentIdField = getId();
//                parentIdField.setAccessible(true);
//                Object parentId = null;
//                try {
//                    parentId = EntityUtils.wrapIdValue(parentIdField.get(this.getPersistentObject()));
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
//                }

                Parent parent = generateParent(childEntity, relation.getMappedBy());

                var childTable = EntityUtils.getTableName(childEntity.getClass());
                var query =
//                      String.format(DELETE_CHILD_NO_CASCADE, childTable, parentIdColumnName, parentIdColumnName,
//                        parentId);
                  String.format(DELETE_CHILD_NO_CASCADE, childTable, parent.getColumnName(), parent.getColumnName(),
                    parent.getId());
//                System.out.println(query);
                getDepthQueryQueue().computeIfAbsent(getQueryDepth() + 1, k -> new HashSet<>()).add(query);
//                    statement.execute(query);
//                } catch (SQLException ex) {
//                    throw new PersistenceException("Could not Execute DELETE statement on child entity", ex);
//                }
            }
        } else {
            System.out.println("LAZY is not yet implemented");
        }
    }

    private <T extends EntityRelation> boolean hasCascadeRelation(T relation) {
        var cascadeList = Arrays.stream(relation.getCascade()).toList();
        return cascadeList.contains(CascadeType.ALL) || cascadeList.contains(CascadeType.DELETE);
    }


}
