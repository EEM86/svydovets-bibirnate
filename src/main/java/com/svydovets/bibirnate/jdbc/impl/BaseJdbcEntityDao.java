package com.svydovets.bibirnate.jdbc.impl;

import static com.svydovets.bibirnate.session.query.CrudOperation.DELETE;
import static com.svydovets.bibirnate.utils.EntityUtils.getFieldName;
import static com.svydovets.bibirnate.utils.EntityUtils.getIdField;
import static com.svydovets.bibirnate.utils.EntityUtils.getTableName;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Optional;

import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.mapper.EntityMapperService;
import com.svydovets.bibirnate.session.query.processor.QueryProcessorFactory;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class BaseJdbcEntityDao implements JdbcEntityDao {
    public static final String SELECT_FROM_TABLE_BY_COLUMN = "select * from %s where %s = ?";
    private final Connection connection;

    @Override
    public <T> Optional<T> findById(Object id, Class<T> type) {
        var field = getIdField(type);
        return findBy(field, id, type);
    }

    @SneakyThrows
    @Override
    public <T> Optional<T> findBy(Field field, Object value, Class<T> type) {
        var tableName = getTableName(type);
        var columnName = getFieldName(field);
        var selectSql = String.format(SELECT_FROM_TABLE_BY_COLUMN, tableName, columnName);

        try (var statement = connection.prepareStatement(selectSql)) {
            statement.setObject(1, value);
            var resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            T entity = EntityMapperService.mapToObject(type, resultSet);
            return Optional.of(entity);
        }
    }

    @SneakyThrows
    @Override
    public void remove(Object entity) {
        var queryProcessor = QueryProcessorFactory.defineQueryProcessor(DELETE, entity, connection);
        queryProcessor.execute();
    }
}