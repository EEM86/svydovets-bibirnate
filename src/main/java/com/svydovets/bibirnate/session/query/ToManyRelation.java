package com.svydovets.bibirnate.session.query;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ToManyRelation extends EntityRelation {

    List<Object> relatedEntities;

    public ToManyRelation(FetchType fetch, CascadeType[] cascade, String mappedBy, List<Object> relatedEntities) {
        setFetch(fetch);
        setCascade(cascade);
        setMappedBy(mappedBy);
        this.relatedEntities = relatedEntities;
    }
}
