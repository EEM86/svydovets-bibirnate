package com.svydovets.bibirnate.configuration.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CacheProperties {

    /**
     * Enables second level cache. Second level cache provides cashing on SessionFactory level.
     */
    boolean enabled = Boolean.FALSE;

    /**
     * Size of the second level cache.
     */
    int size = 20_000;
}
