package com.svydovets.bibirnate.session.query;

import java.lang.reflect.Field;
import java.util.List;

import com.svydovets.bibirnate.annotation.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {@link OneToMany} specific {@link EntityRelation} POJO.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ToManyRelation extends EntityRelation {

    List<Object> relatedEntities;

    public ToManyRelation(FetchType fetch, CascadeType[] cascade, String mappedBy,
                          List<Object> relatedEntities, Field field) {
        setFetch(fetch);
        setCascade(cascade);
        setMappedBy(mappedBy);
        setField(field);
        this.relatedEntities = relatedEntities;
    }
}
