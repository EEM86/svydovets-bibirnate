package com.svydovets.bibirnate.demo.entity;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.OneToOne;
import com.svydovets.bibirnate.annotation.Table;

import lombok.Data;

@Entity
@Table(name = "k")
@Data
public class SomeToOneRelation {

    @Id
    private Long id;

    private String text;

    @OneToOne(mappedBy = "relation")
    private Note note;
}
