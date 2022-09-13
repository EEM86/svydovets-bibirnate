package com.svydovets.bibirnate.entities;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.JoinColumn;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.Table;
import com.svydovets.bibirnate.demo.entity.Person;

import lombok.Data;

@Entity
@Table(name = "notes")
@Data
public class Note {

    @Id
    private Long id;

    private String note;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person owner;
}
