package com.svydovets.bibirnate.entity;

import java.util.Objects;

public class TestEntity {

    private int version;

    public TestEntity() {
    }

    public TestEntity(int version) {
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestEntity)) return false;
        TestEntity that = (TestEntity) o;
        return getVersion() == that.getVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVersion());
    }
}
