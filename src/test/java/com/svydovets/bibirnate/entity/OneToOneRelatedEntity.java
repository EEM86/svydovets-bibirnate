package com.svydovets.bibirnate.entity;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;

import lombok.Data;

@Entity
@Data
public class OneToOneRelatedEntity {

    @Id
    private Long id;

}
