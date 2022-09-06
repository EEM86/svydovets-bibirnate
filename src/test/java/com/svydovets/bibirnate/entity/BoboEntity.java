package com.svydovets.bibirnate.entity;

import java.util.Objects;

import com.svydovets.bibirnate.annotation.Cacheable;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;

@Entity
@Cacheable
public class BoboEntity {
    @Id
    private long id;

    public BoboEntity() {
    }

    public BoboEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoboEntity)) return false;
        BoboEntity that = (BoboEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
