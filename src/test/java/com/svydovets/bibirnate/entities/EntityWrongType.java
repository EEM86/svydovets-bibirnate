package com.svydovets.bibirnate.entities;

import java.util.List;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.OneToOne;

import lombok.Data;

@Entity
@Data
public class EntityWrongType {

    private AllTypesEntity entity;

    @ManyToOne
    private AllTypesEntity manyToOneEntity;

    @OneToOne
    private AllTypesEntity oneToOneEntity;

    @OneToMany(mappedBy = "")
    private List<AllTypesEntity> oneToManyEntities;

}
