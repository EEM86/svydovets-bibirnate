package com.svydovets.bibirnate.demo.entity;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.JoinColumn;
import com.svydovets.bibirnate.annotation.ManyToOne;
import com.svydovets.bibirnate.annotation.Table;

import lombok.Data;

@Entity
@Table(name = "note_descriptions")
@Data
public class NoteDescription {

    @Id
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;
}
