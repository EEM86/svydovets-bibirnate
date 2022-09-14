package com.svydovets.bibirnate.session.query;

import java.util.List;
import java.util.Set;

public interface Query {

    Object getSingleResult();

    Object getFirstResult();

    <T> List<T> getResultList();

    <T> Set<T> getResultSet();

    void execute();

    void addParameter(Object parameter);
}
