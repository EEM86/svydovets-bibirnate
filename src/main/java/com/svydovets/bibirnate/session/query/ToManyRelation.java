package com.svydovets.bibirnate.session.query;

import java.lang.reflect.Field;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ToManyRelation extends EntityRelation {

    List<Object> relatedEntities;

    public ToManyRelation(FetchType fetch, CascadeType[] cascade, String mappedBy, List<Object> relatedEntities, Field field) {
        setFetch(fetch);
        setCascade(cascade);
        setMappedBy(mappedBy);
        setField(field);
        this.relatedEntities = relatedEntities;
    }
}
