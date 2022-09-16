package com.svydovets.bibirnate.entities;

import java.util.List;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.Table;
import com.svydovets.bibirnate.annotation.Transient;
import com.svydovets.bibirnate.session.query.CascadeType;

import lombok.Data;


@Entity
@Table(name = "persons")
@Data
public class PersonRelationsEntity {

    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Transient
    private String blindField;

    @OneToMany(mappedBy = "note", cascade = CascadeType.DELETE)
    private List<Note> notes;

}
