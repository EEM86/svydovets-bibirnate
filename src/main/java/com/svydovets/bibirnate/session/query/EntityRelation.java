package com.svydovets.bibirnate.session.query;

import lombok.Data;

@Data
public class EntityRelation {

    private FetchType fetch;

    private CascadeType[] cascade;

    private String mappedBy;
}
