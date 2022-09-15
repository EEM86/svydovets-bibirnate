package com.svydovets.bibirnate.cache.key.parameters;

import java.util.Collection;
import java.util.Objects;

import lombok.ToString;

/**
 * This class is extension for the {@link AbstractKeyParam} that builds on the entity class type, query (original query
 * to the DB), and collection class type of result.
 */
@ToString
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof QueryKeyParam<?> that)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return getQuery().equals(that.getQuery()) && getCollectionType().equals(that.getCollectionType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getQuery(), getCollectionType());
    }
}
