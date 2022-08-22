package com.svydovets.bibirnate.entities;

import com.svydovets.bibirnate.annotation.Entity;

import lombok.Data;

@Entity
@Data
public class EntityWrongType {

    private AllTypesEntity entity;

}
