package com.svydovets.bibirnate.cache.key;

import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;

import java.time.LocalDateTime;
import java.util.Objects;

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

    /**
     * Updates time to {@link LocalDateTime#now()}
     */
    public void update() {
        this.updated = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Key)) return false;
        Key<?> key = (Key<?>) o;
        return abstractKeyParam.equals(key.abstractKeyParam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abstractKeyParam);
    }

}
