package com.svydovets.bibirnate.cache.key;

import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;

import java.time.LocalDateTime;
import java.util.Objects;

public class Key<T> {

    private final AbstractKeyParam<T> abstractKeyParam;
    private LocalDateTime updated;

    public Key(AbstractKeyParam<T> abstractKeyParam) {
        this.abstractKeyParam = abstractKeyParam;
        this.updated = LocalDateTime.now();
    }

    public AbstractKeyParam<T> getKeyParam() {
        return abstractKeyParam;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void update() {
        this.updated = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Key)) return false;
        Key<?> key = (Key<?>) o;
        return abstractKeyParam.equals(key.abstractKeyParam) && getUpdated().equals(key.getUpdated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(abstractKeyParam);
    }

}
