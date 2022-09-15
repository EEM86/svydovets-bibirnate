package com.svydovets.bibirnate.demo.entity;

import java.util.List;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.OneToMany;
import com.svydovets.bibirnate.annotation.OneToOne;
import com.svydovets.bibirnate.annotation.Table;
import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.FetchType;

import lombok.Data;

@Entity
@Table(name = "t")
@Data
public class Test {

    @Id
    private Long id;

    private String test;

    @OneToMany(mappedBy = "test", cascade = CascadeType.DELETE, fetch = FetchType.EAGER)
    private List<Person> personList;

    @OneToOne(mappedBy = "test", cascade = CascadeType.DELETE)
    private Note note;
}
