package com.svydovets.bibirnate.entity;

import java.util.Objects;

import com.svydovets.bibirnate.annotation.Cacheable;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;

@Entity
@Cacheable
public class BiberEntity {

    @Id
    private long id;

    public BiberEntity() {
    }

    public BiberEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BiberEntity)) return false;
        BiberEntity that = (BiberEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
