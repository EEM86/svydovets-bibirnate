package com.svydovets.bibirnate.session.query.processor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.svydovets.bibirnate.exceptions.PersistenceException;
import com.svydovets.bibirnate.logs.SqlLogger;
import com.svydovets.bibirnate.utils.EntityUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        var valueList = new ArrayList<String>();
        for (var field : fields) {
            field.setAccessible(true);
            final String columnName = EntityUtils.getColumnName(field);
            if (columnName != null && columnName.equals("id")) {
                continue;
            }
            columnNames.append(columnName).append(',');
            final Object persistentObject = getPersistentObject();
            final String wrappedValue =  String.valueOf(EntityUtils.wrapIdValue(field.get(persistentObject)));
            valueList.add(wrappedValue);
        }
        columnNames.deleteCharAt(columnNames.length() - 1).append(')');
        final String formattedQuery = String.format(INSERT_TEMPLATE, columnNames, valueList.stream()
                .collect(Collectors.joining(",")));
        getSqlLogger().log("***********" + formattedQuery);
        return formattedQuery;
    }

    @Override
    public void execute() {
        String query = generateQuery();
        try (var statement = getConnection().createStatement()) {
            log.trace("Executing insert statement.");
            sqlLogger.log(query);
            statement.execute(query);
            log.trace("Insertion was successfull.");
        } catch (SQLException ex) {
            throw new PersistenceException("Insertion statement failed.", ex);
        }
    }
}
