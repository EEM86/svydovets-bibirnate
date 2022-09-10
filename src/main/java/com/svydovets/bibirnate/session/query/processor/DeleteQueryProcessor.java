package com.svydovets.bibirnate.session.query.processor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import com.svydovets.bibirnate.exceptions.PersistenceException;
import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.EntityRelation;

import lombok.SneakyThrows;

public class DeleteQueryProcessor extends QueryProcessor {

    private static final String DELETE_TEMPLATE = "DELETE FROM %s WHERE %s = %s";

    public DeleteQueryProcessor(Object entity, Connection connection) {
        super(entity, connection);
    }

    public DeleteQueryProcessor(Object entity, Connection connection, Field parentId) {
        super(entity, connection, parentId);
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
            //todo: handle child deletion
            return String.format(DELETE_TEMPLATE, this.getTableName(), "parent field", getParentId());
        }
    }

    @SneakyThrows
    @Override
    public void execute() {
        try (var statement = getConnection().createStatement()) {
            if (hasToManyRelations()) {
                //todo: handle OneToMany biDir + uniDir
                handleToManyRelations();
            }
            if (hasToOneRelations()) {
            //todo: will be handled in relations ticket
            //          OneToOne biDir + uniDir
            //          ManyToOne biDir + uniDir
            }
            String sql = generateQuery();
            statement.execute(sql);
        } catch (SQLException ex) {
            throw new PersistenceException("Could not Execute DELETE statement", ex);
        } finally {
            getConnection().close();
        }
    }

    private void handleToManyRelations() {
        getToManyRelations().forEach(
          toManyRelation -> {
              if (hasCascadeRelation(toManyRelation)) {
                  //todo: will be done in RELATIONS ticket
                  DeleteQueryProcessor innerProcessor = new DeleteQueryProcessor(
                    toManyRelation.getRelatedEntities().get(0).getClass(), getConnection(), getId());
                  innerProcessor.execute();
              } else {
                  //todo: only set parent id to null
              }
          }
        );
    }

    private <T extends EntityRelation> boolean hasCascadeRelation(T relation) {
        var cascadeList = Arrays.stream(relation.getCascade()).toList();
        return cascadeList.contains(CascadeType.ALL) || cascadeList.contains(CascadeType.DELETE);
    }


}
