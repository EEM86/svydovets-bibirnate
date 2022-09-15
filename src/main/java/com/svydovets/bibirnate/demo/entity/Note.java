package com.svydovets.bibirnate.demo.entity;

import java.util.List;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.JoinColumn;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.OneToOne;
import com.svydovets.bibirnate.annotation.Table;
import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.FetchType;

import lombok.Data;

@Entity
@Table(name = "notes")
@Data
public class Note {

    @Id
    private Long id;

    @Column(name = "body")
    private String body;

    private String note;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToMany(mappedBy = "note", cascade = CascadeType.DELETE, fetch = FetchType.EAGER)
    private List<NoteDescription> descriptions;

    @OneToOne(cascade = CascadeType.DELETE)
    @JoinColumn(name = "relation_id")
    private SomeToOneRelation relation;
}
