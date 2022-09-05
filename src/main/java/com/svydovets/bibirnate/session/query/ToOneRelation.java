package com.svydovets.bibirnate.session.query;

public class ToOneRelation extends EntityRelation {

    private Object relatedEntity;

    public ToOneRelation(FetchType fetch, CascadeType[] cascade, Object relatedEntity) {
        super(fetch, cascade);
        this.relatedEntity = relatedEntity;
    }
}
