package com.svydovets.bibirnate.session.query;

import java.lang.reflect.Field;

import lombok.Data;

@Data
public class EntityRelation {

    private FetchType fetch;

    private CascadeType[] cascade;

    private String mappedBy;

    private Field field;
}
