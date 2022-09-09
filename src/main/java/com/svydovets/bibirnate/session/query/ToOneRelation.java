package com.svydovets.bibirnate.session.query;

import lombok.Data;

@Data
public class ToOneRelation extends EntityRelation {

    private Object relatedEntity;

    public ToOneRelation(FetchType fetch, CascadeType[] cascade, Object relatedEntity) {
        setFetch(fetch);
        setCascade(cascade);
        this.relatedEntity = relatedEntity;
    }
}
