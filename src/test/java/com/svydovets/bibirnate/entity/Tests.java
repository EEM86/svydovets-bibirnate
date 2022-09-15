package com.svydovets.bibirnate.entity;

import java.time.LocalDateTime;

import com.svydovets.bibirnate.annotation.Cacheable;
import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.Table;

import lombok.Data;

@Entity
@Table(name = "tests")
@Data
@Cacheable
public class Tests {

    @Id
    private Integer id;
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
