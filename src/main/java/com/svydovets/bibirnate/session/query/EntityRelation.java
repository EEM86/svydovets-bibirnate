package com.svydovets.bibirnate.session.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntityRelation {

    private FetchType fetch;

    private CascadeType[] cascade;
}
