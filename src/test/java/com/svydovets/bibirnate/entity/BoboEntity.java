package com.svydovets.bibirnate.entity;

import java.util.Objects;

public class BoboEntity {
    private long version;

    public BoboEntity() {
    }

    public BoboEntity(long version) {
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoboEntity)) return false;
        BoboEntity that = (BoboEntity) o;
        return getVersion() == that.getVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVersion());
    }
}
