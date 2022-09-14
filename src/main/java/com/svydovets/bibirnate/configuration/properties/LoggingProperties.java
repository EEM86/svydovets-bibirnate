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
public class LoggingProperties {
    /**
     * Enable/Disable logging for SQL queries.
     */
    private boolean enabled = Boolean.FALSE;
}
