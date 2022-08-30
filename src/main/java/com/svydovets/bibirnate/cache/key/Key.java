package com.svydovets.bibirnate.cache.key;

import java.time.LocalDateTime;
import java.util.Objects;

import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;

/**
 * This class provides a key for LRU (last recently used) cache.
 * For defining LRU here we are using <b>updated</b> field. When someone extracts cache by Key then we use
 * {@link  Key#update()}.
 * For providing unique values in the Key we are using {@link AbstractKeyParam} <b>abstractKeyParam</b> field that
 * constructed in the way to provide unique indicators for Key.
 */
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Key<?> key)) {
            return false;
        }
        return abstractKeyParam.equals(key.abstractKeyParam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abstractKeyParam);
    }

}
