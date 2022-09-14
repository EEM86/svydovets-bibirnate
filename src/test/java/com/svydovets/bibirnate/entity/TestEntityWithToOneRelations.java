package com.svydovets.bibirnate.entity;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.JoinColumn;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToOne;

import lombok.Data;

@Entity
@Data
public class TestEntityWithToOneRelations {

    @Id
    private Long id;

    @OneToOne
    private OneToOneRelatedEntity oneToOneRelatedEntityWithoutJoinColumn;

    @OneToOne
    @JoinColumn(name = "one_to_one_related_entity_id")
    private OneToOneRelatedEntity oneToOneRelatedEntity;

    @ManyToOne
    private ManyToOneRelatedEntity manyToOneRelatedEntityWithoutJoinColumn;

    @ManyToOne
    @JoinColumn(name = "many_to_one_related_entity_id")
    private ManyToOneRelatedEntity manyToOneRelatedEntity;

}
