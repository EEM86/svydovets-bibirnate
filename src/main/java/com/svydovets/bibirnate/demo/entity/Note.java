package com.svydovets.bibirnate.demo.entity;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.Table;

import lombok.Data;

@Entity
@Table(name = "notes")
@Data
public class Note {

    @Id
    private Long id;
    private String body;

    private Person person;
}
