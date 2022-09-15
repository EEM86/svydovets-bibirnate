package com.svydovets.bibirnate.session.query;

import java.util.List;
import java.util.Set;

import com.svydovets.bibirnate.exceptions.NoResultException;
import com.svydovets.bibirnate.exceptions.NoUniqueResultException;

/**
 * Basic contract for creation Queries.
 */
public interface Query {

    /**
     * Provides single result from execution query.
     * In case if no result found -> throws {@link NoResultException}, in case if more than one result found ->
     * throws {@link NoUniqueResultException}.
     *
     * @return extracted entity by SQL query.
     */
    Object getSingleResult();

    /**
     * Provides first result from execution query.
     * In case if more than one result found -> throws {@link NoUniqueResultException}.
     *
     * @return extracted entity by SQL query.
     */
    Object getFirstResult();

    /**
     * Provides result {@link List} from execution query.
     *
     * @return {@link List} of extracted entity by SQL query.
     */
    <T> List<T> getResultList();

    /**
     * Provides result {@link Set} from execution query.
     *
     * @return {@link Set} of extracted entity by SQL query.
     */
    <T> Set<T> getResultSet();

    /**
     * Execute provided SQL query. Usually uses for INSERT, UPDATE, DELETE operations.
     */
    void execute();

    /**
     * Adds parameter to the query statement instead of <b>?</b>.
     * Pay attention that order of included parameters should be the same as in query.
     *
     * @param parameter parameter that should be added to the SQL query.
     */
    void addParameter(Object parameter);
}
