package com.svydovets.bibirnate.entities;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Entity;

@Entity
public class EntityWithoutId {

    @Column(name = "last_name")
    private String lastName;
}
