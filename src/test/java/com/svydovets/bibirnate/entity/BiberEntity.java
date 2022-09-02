package com.svydovets.bibirnate.entity;

import java.util.Objects;

public class BiberEntity {

    private long version;

    public BiberEntity() {
    }

    public BiberEntity(long version) {
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BiberEntity)) return false;
        BiberEntity that = (BiberEntity) o;
        return getVersion() == that.getVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVersion());
    }
}
