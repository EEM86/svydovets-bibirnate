package com.svydovets.bibirnate.session.query;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ToManyRelation extends EntityRelation {

    List<Object> relatedEntities;

    public ToManyRelation(FetchType fetch, CascadeType[] cascade, List<Object> relatedEntities) {
        setFetch(fetch);
        setCascade(cascade);
        this.relatedEntities = relatedEntities;
    }
}
