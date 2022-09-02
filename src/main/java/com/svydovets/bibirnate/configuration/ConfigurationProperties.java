package com.svydovets.bibirnate.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfigurationProperties {
    /**
     * Specifies the configuration parameters for database connection.
     */
    private DatabaseProperties database;
}
