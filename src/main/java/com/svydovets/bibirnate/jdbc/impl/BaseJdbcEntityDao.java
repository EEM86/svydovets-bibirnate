package com.svydovets.bibirnate.jdbc.impl;

import static com.svydovets.bibirnate.session.query.CrudOperation.DELETE;
import static com.svydovets.bibirnate.utils.EntityUtils.getColumnName;
import static com.svydovets.bibirnate.utils.EntityUtils.getIdField;
import static com.svydovets.bibirnate.utils.EntityUtils.getTableName;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Objects;
import java.util.Optional;

import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.logs.SqlLogger;
import com.svydovets.bibirnate.mapper.EntityMapperService;
import com.svydovets.bibirnate.session.query.processor.QueryProcessorFactory;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Standard JdbcEntityDao implementation.
 */
public class BaseJdbcEntityDao implements JdbcEntityDao {
    public static final String SELECT_FROM_TABLE_BY_COLUMN = "select * from %s where %s = ?";
    private final Connection connection;

    protected final SqlLogger sqlLogger;

    private final EntityMapperService entityMapperService;

    public BaseJdbcEntityDao(Connection connection, SqlLogger sqlLogger) {
        this.connection = connection;
        this.entityMapperService = new EntityMapperService(this);
        this.sqlLogger = sqlLogger;
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public <T> Optional<T> findById(Object id, Class<T> type) {
        var field = getIdField(type);
        return findBy(field, id, type);
    }

    /**
     * {@inheritDoc}
     **/
    @SneakyThrows
    @Override
    public <T> Optional<T> findBy(Field field, Object value, Class<T> type) {
        var tableName = getTableName(type);
        var columnName = getColumnName(field);
        var selectSql = String.format(SELECT_FROM_TABLE_BY_COLUMN, tableName, columnName);

        try (var statement = connection.prepareStatement(selectSql)) {
            statement.setObject(1, value);
            var resultSet = statement.executeQuery();
            sqlLogger.log(statement.unwrap(java.sql.PreparedStatement.class).toString());

            if (!resultSet.next()) {
                return Optional.empty();
            }

            T entity = entityMapperService.mapToObject(type, resultSet);
            return Optional.of(entity);
        }
    }

    /**
     * {@inheritDoc}
     **/
    @SneakyThrows
    @Override
    public void remove(Object entity) {
        var queryProcessor = QueryProcessorFactory.defineQueryProcessor(DELETE, entity, connection,
          sqlLogger);
        queryProcessor.execute();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public EntityMapperService getEntityMapperService() {
        return entityMapperService;
    }
}
