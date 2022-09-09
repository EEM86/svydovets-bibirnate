package com.svydovets.bibirnate.entities;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.Table;
import com.svydovets.bibirnate.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "persons")
@Data
@AllArgsConstructor
public class PersonSimpleEntity {

    @Id
    private Long id;

    private String first_name;

    private String last_name;

    @Transient
    private String blindField;

}
