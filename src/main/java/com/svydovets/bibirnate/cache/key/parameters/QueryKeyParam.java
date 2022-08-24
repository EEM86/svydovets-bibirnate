package com.svydovets.bibirnate.cache.key.parameters;

import java.util.Collection;
import java.util.Objects;

/**
 * This class is extension for the {@link AbstractKeyParam} that builds on the entity class type, query (original query
 * to the DB), and collection class type of result.
 */
public class QueryKeyParam<T> extends AbstractKeyParam<T> {

    private final String query;
    private final Class<? extends Collection> collectionType;

    public QueryKeyParam(Class<T> entityType, String query, Class<? extends Collection> collectionType) {
        super(entityType);
        this.query = query;
        this.collectionType = collectionType;
    }

    public String getQuery() {
        return query;
    }

    public Class<? extends Collection> getCollectionType() {
        return collectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryKeyParam)) return false;
        if (!super.equals(o)) return false;
        QueryKeyParam<?> that = (QueryKeyParam<?>) o;
        return getQuery().equals(that.getQuery()) && getCollectionType().equals(that.getCollectionType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getQuery(), getCollectionType());
    }
}
