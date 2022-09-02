package com.svydovets.bibirnate.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DatabaseProperties {
    /**
     * Specifies the url for database connection.
     */
    @NotNull
    private String url;
    /**
     * Specifies the user for database connection.
     */
    @NotNull
    private String user;
    /**
     * Specifies the password for database connection.
     */
    @NotNull
    private String password;
    /**
     * Specifies the driver name for database connection.
     */
    private String driverName;
}
