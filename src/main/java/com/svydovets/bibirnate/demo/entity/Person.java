package com.svydovets.bibirnate.demo.entity;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "persons")
public class Person {
    @Id
    private Integer id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
}
