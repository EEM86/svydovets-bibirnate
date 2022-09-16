package com.svydovets.bibirnate.demo.entity;

import com.svydovets.bibirnate.annotation.Cacheable;
import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.JoinColumn;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.Table;

import lombok.Data;

@Entity
@Table(name = "notes")
@Data
@Cacheable
public class Note {

    @Id
    private Long id;

    @Column(name = "body")
    private String body;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
