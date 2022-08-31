package com.svydovets.bibirnate.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfigurationProperties {
    /**
     * Specifies the url for database connection.
     */
    @NotNull
    private String databaseUrl;
    /**
     * Specifies the user for database connection.
     */
    @NotNull
    private String databaseUser;
    /**
     * Specifies the password for database connection.
     */
    @NotNull
    private String databasePassword;
    /**
     * Specifies the driver name for database connection.
     */
    private String databaseDriverName;
}
