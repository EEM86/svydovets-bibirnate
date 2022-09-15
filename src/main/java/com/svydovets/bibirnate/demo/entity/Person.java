package com.svydovets.bibirnate.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.Table;
import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.FetchType;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Table(name = "persons")
public class Person {
    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private int age;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "person", cascade = CascadeType.DELETE, fetch = FetchType.EAGER)
    private List<Note> notes;
}
