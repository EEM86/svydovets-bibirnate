package com.svydovets.bibirnate.session.impl;

import static com.svydovets.bibirnate.utils.EntityUtils.getFieldName;
import static com.svydovets.bibirnate.utils.EntityUtils.getIdField;
import static com.svydovets.bibirnate.utils.EntityUtils.getTableName;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Optional;

import com.svydovets.bibirnate.mapper.EntityMapperService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class JdbcEntityDao {
    public static final String SELECT_FROM_TABLE_BY_COLUMN = "select * from %s where %s = ?";
    private final Connection connection;

    public <T> Optional<T> findById(Object id, Class<T> type) {
        var field = getIdField(type);
        return findBy(field, id, type);
    }

    @SneakyThrows
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
}
