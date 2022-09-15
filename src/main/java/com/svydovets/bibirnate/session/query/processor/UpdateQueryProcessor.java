package com.svydovets.bibirnate.session.query.processor;

import com.svydovets.bibirnate.exceptions.PersistenceException;
import com.svydovets.bibirnate.utils.EntityUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class UpdateQueryProcessor extends QueryProcessor {

    private static final String UPDATE_TEMPLATE = "UPDATE %s SET %s WHERE %s";

    protected UpdateQueryProcessor(Object entity, Connection connection) {
        super(entity, connection);
    }

    @Override
    public String generateQuery() {
        return prepareUpdateByQuery(getEntity(), getId());
    }

    @Override
    public void execute() {
        try (var statement = getConnection().createStatement()) {
            var sql = generateQuery();
            statement.execute(sql);
        } catch (SQLException ex) {
            log.trace("Could not update entity with id = {}", this.getId());
            throw new PersistenceException("Could not Execute UPDATE statement", ex);
        }
        log.trace("Successfully updated entity with id = {}", this.getId());
    }

    public String prepareUpdateByQuery(Object entity, Field searchedField) {
        var columns = new StringBuilder();

        getEntityFields().stream()
                .filter(EntityUtils::isRegularField)
                .forEach(field -> columns
                        .append(EntityUtils.resolveColumnName(field))
                        .append("=")
                        .append((field.getType() == String.class) ? "'" : "")
                        .append(EntityUtils.getFieldValue(field, entity))
                        .append((field.getType() == String.class) ? "'" : "")
                        .append(","));

        columns.deleteCharAt(columns.lastIndexOf(","));

        var condition = "%s=%s".formatted(
                EntityUtils.resolveColumnName(searchedField),
                EntityUtils.getFieldValue(searchedField, entity));

        return String.format(UPDATE_TEMPLATE, this.getTableName(), columns, condition);
    }
}
