package com.svydovets.bibirnate.session.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.cache.CacheUtils;
import com.svydovets.bibirnate.exceptions.BibernateException;
import com.svydovets.bibirnate.exceptions.NoResultException;
import com.svydovets.bibirnate.exceptions.NoUniqueResultException;
import com.svydovets.bibirnate.mapper.EntityMapperService;

/**
 * This class is the service that provides an opportunity to work with native SQL queries for specified entity type.
 * Pay attention that syntax for queries the same as for JDBC {@link PreparedStatement}, like:
 * "SELECT * FROM TABLE_NAME WHERE TABLE_COlUMN_1 = ?" or
 * "SELECT * FROM TABLE_NAME WHERE TABLE_COlUMN_1 LIKE ?"
 */
public class TypedQuery implements Query {

    private final Connection connection;
    private final String query;
    private final List<Object> parameters;
    private final Class<?> entityType;
    private final CacheContainer cacheContainer;

    public TypedQuery(Connection connection, String query, Class<?> entityType, CacheContainer cacheContainer) {
        this.connection = connection;
        this.query = query;
        this.entityType = entityType;
        this.cacheContainer = cacheContainer;
        this.parameters = new ArrayList<>();
    }

    @Override
    public Object getSingleResult() {
        List<Object> queryResult = getQueryResult();

        if (queryResult.isEmpty()) {
            throw new NoResultException(String.format("No result found by the query [%s].", this.query));
        } else if (queryResult.size() > 1) {
            throw new NoUniqueResultException(String.format("Not unique result by the query [%s].", this.query));
        }

        return queryResult.get(0);
    }

    @Override
    public Object getFirstResult() {
        List<Object> queryResult = getQueryResult();
        if (queryResult.isEmpty()) {
            throw new NoResultException(String.format("No result found by the query [%s].", this.query));
        }
        return queryResult.get(0);
    }

    @Override
    public <T> List<T> getResultList() {
        return getQueryResult();
    }

    @Override
    public <T> Set<T> getResultSet() {
        return new HashSet<>(getResultList());
    }

    @Override
    public void execute() {
        try (var statement = connection.prepareStatement(query)) {
            initializePreparedStatement(statement);
            String extractedQuery = extractQuery(statement);

            statement.executeUpdate();

            if (!StringUtils.startsWithIgnoreCase(extractedQuery, "select")) {
                CacheUtils.getSimilarQueryKey(cacheContainer, entityType)
                  .ifPresent(key -> CacheUtils.invalidate(cacheContainer, key));
            }
        } catch (SQLException ex) {
            throw generateBibernateException(ex);
        }
    }

    @Override
    public void addParameter(Object parameter) {
        parameters.add(parameter);
    }

    private <T> List<T> getQueryResult() {
        List<T> result = new ArrayList<>();
        try (var statement = connection.prepareStatement(query)) {
            initializePreparedStatement(statement);

            String extractedQuery = extractQuery(statement);
            Optional<? extends Collection> fromCache = CacheUtils.extract(cacheContainer, entityType, extractedQuery,
              result.getClass());
            if (fromCache.isEmpty()) {
                var resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    result.add(EntityMapperService.mapToObject((Class<T>) entityType, resultSet));
                }
                CacheUtils.put(cacheContainer, entityType, extractedQuery, result.getClass(), result);
            } else {
                result = (List<T>) fromCache.get();
            }

            return result;
        } catch (SQLException ex) {
            throw generateBibernateException(ex);
        }
    }

    private void initializePreparedStatement(PreparedStatement statement) throws SQLException {
        if (!parameters.isEmpty()) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
        }
    }

    private String extractQuery(PreparedStatement statement) {
        return statement.toString().split("wrapping")[1].trim();
    }

    private BibernateException generateBibernateException(SQLException ex) {
        return new BibernateException(String.format("Cannot execute passed SQL query [%s]. For the details look "
          + "at stacktrace", query), ex);
    }

}
