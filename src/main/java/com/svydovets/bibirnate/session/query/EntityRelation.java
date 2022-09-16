package com.svydovets.bibirnate.session.query;

import java.lang.reflect.Field;

import lombok.Data;

/**
 * Basic POJO class that contains all the params child entity is mapped with.
 */
@Data
public class EntityRelation {

    private FetchType fetch;

    private CascadeType[] cascade;

    private String mappedBy;

    private Field field;
}
