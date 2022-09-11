package com.svydovets.bibirnate.configuration;

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
     * Enables second level cache.
     */
    boolean secondLevelCacheEnabled = Boolean.FALSE;
}
