package com.svydovets.bibirnate.entities;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;

@Entity
public class EntityWithTwoIds {

    @Id
    private long id;

    @Id
    private String uuid;
}
