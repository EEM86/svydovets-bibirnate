package com.svydovets.bibirnate.session.query;

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
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is the service that provides an opportunity to work with native SQL queries for specified entity type.
 * Pay attention that syntax for queries the same as for JDBC {@link PreparedStatement}, like:
 * "SELECT * FROM TABLE_NAME WHERE TABLE_COlUMN_1 = ?" or
 * "SELECT * FROM TABLE_NAME WHERE TABLE_COlUMN_1 LIKE ?"
 */
@Slf4j
public class TypedQuery implements Query {

    private final JdbcEntityDao jdbcEntityDao;
    private final String query;
    private final List<Object> parameters;
    private final Class<?> entityType;
    private final CacheContainer cacheContainer;

    public TypedQuery(JdbcEntityDao jdbcEntityDao, String query, Class<?> entityType, CacheContainer cacheContainer) {
        this.jdbcEntityDao = jdbcEntityDao;
        this.query = query;
        this.entityType = entityType;
        this.cacheContainer = cacheContainer;
        this.parameters = new ArrayList<>();
        log.trace("New TypedQuery fro SQL [{}] is created", query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getSingleResult() {
        log.trace("getSingleResult, start extraction single result by query [{}]", query);
        List<Object> queryResult = getQueryResult();

        if (queryResult.isEmpty()) {
            log.error("getSingleResult, no result found by the query [{}]", query);
            throw new NoResultException(String.format("No result found by the query [%s].", this.query));
        } else if (queryResult.size() > 1) {
            log.error("getSingleResult, no unique result found by the query [{}]", query);
            throw new NoUniqueResultException(String.format("Not unique result by the query [%s].", this.query));
        }

        log.trace("getSingleResult, extraction single result by query [{}] is finished with state [{}]", query,
          queryResult.get(0));
        return queryResult.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getFirstResult() {
        log.trace("getFirstResult, start extraction first result by query [{}]", query);
        List<Object> queryResult = getQueryResult();
        if (queryResult.isEmpty()) {
            throw new NoResultException(String.format("No result found by the query [%s].", this.query));
        }

        log.trace("getFirstResult, extraction first result by query [{}] is finished with state [{}]", query,
          queryResult.get(0));
        return queryResult.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getResultList() {
        log.trace("getResultList, extraction result list by query [{}]", query);
        return getQueryResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Set<T> getResultSet() {
        log.trace("getResultSet, extraction result set by query [{}]", query);
        return new HashSet<>(getResultList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.trace("execute, start execution query [{}]", query);
        try (var statement = jdbcEntityDao.getConnection().prepareStatement(query)) {
            initializePreparedStatement(statement);
            String extractedQuery = extractQuery(statement);

            statement.executeUpdate();

            if (!StringUtils.startsWithIgnoreCase(extractedQuery, "select")) {
                CacheUtils.getSimilarQueryKey(cacheContainer, entityType)
                  .ifPresent(key -> CacheUtils.invalidate(cacheContainer, key));
            }
        } catch (SQLException ex) {
            log.trace("execute, cannot execute query [{}]. Failed with message [{}]", query, ex.getMessage());
            throw generateBibernateException(ex);
        }
        log.trace("execute, execution query [{}] is finished successfully", query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addParameter(Object parameter) {
        parameters.add(parameter);
    }

    private <T> List<T> getQueryResult() {
        log.trace("getQueryResult, start extraction by the query [{}]", query);
        List<T> result = new ArrayList<>();
        try (var statement = jdbcEntityDao.getConnection().prepareStatement(query)) {
            initializePreparedStatement(statement);

            String extractedQuery = extractQuery(statement);
            Optional<? extends Collection> fromCache = CacheUtils.extract(cacheContainer, entityType, extractedQuery,
              result.getClass());
            if (fromCache.isEmpty()) {
                var resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    result.add(jdbcEntityDao.getEntityMapperService().mapToObject((Class<T>) entityType, resultSet));
                }
                CacheUtils.put(cacheContainer, entityType, extractedQuery, result.getClass(), result);
            } else {
                result = (List<T>) fromCache.get();
            }

            log.trace("getQueryResult, extraction by the query [{}] is finished successfully.", query);
            return result;
        } catch (SQLException ex) {
            log.error("getQueryResult, cannot extract values by the query [{}]. Failed with message [{}]", query,
              ex.getMessage());
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
