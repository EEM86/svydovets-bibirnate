package com.svydovets.bibirnate.session.query;

import java.lang.reflect.Field;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ToOneRelation extends EntityRelation {

    private Object relatedEntity;

    private String mappedBy;

    public ToOneRelation(FetchType fetch, CascadeType[] cascade, Object relatedEntity, Field field, String mappedBy) {
        setFetch(fetch);
        setCascade(cascade);
        setMappedBy(mappedBy);
        setField(field);
        this.relatedEntity = relatedEntity;
    }
}
