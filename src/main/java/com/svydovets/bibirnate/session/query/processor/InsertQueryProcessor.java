package com.svydovets.bibirnate.session.query.processor;

import java.sql.Connection;
import java.sql.SQLException;

import com.svydovets.bibirnate.exceptions.PersistenceException;
import com.svydovets.bibirnate.logs.SqlLogger;

import lombok.SneakyThrows;

public class InsertQueryProcessor extends QueryProcessor {

    private static final String INSERT_TEMPLATE = "INSERT INTO %s VALUES(%s)";


    protected InsertQueryProcessor(Object entity, Connection connection, SqlLogger sqlLogger) {
        super(entity, connection, sqlLogger);
    }

    protected InsertQueryProcessor(Object entity, Connection connection, Parent parent, SqlLogger sqlLogger) {
        super(entity, connection, parent, sqlLogger);
    }

    /**
     * Generate insert query, find column names and column values.
     *
     * @return String sqlQuery
     */
    @SneakyThrows
    @Override
    public String generateQuery() {
        var fields = getEntityFields();
        var columnNames = new StringBuilder().append(getTableName()).append('(');
        var columnValues = new StringBuilder().append('(');
        for (var field : fields) {
            field.setAccessible(true);
            columnNames.append(field.getName()).append(',');
            columnNames.append(field.get(getPersistentObject())).append(',');

        }
        // delete last comma from a string builder
        columnNames.deleteCharAt(columnNames.length() - 1).append(')');
        columnValues.deleteCharAt(columnValues.length() - 1).append(')');
        final String formattedQuery = String.format(INSERT_TEMPLATE, columnNames, columnValues);
        getSqlLogger().log("***********" + formattedQuery);
        return formattedQuery;
    }

    @Override
    public void execute() {
        String query = generateQuery();
        sqlLogger.log("Executing insert statement.");
        try (var statement = getConnection().createStatement()) {
            statement.execute(query);
        } catch (SQLException ex) {
            throw new PersistenceException("Insertion statement failed.", ex);
        }
    }
}
