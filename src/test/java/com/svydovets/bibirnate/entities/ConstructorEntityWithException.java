package com.svydovets.bibirnate.entities;

import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;

import lombok.Data;

@Entity
@Data
public class ConstructorEntityWithException {

    @Id
    private Long id;

    public ConstructorEntityWithException() {
        throw new RuntimeException();
    }
}
