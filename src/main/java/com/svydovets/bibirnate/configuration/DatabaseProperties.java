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
public class DatabaseProperties {
    /**
     * Specifies the url for database connection.
     */
    @NotNull
    String url;
    /**
     * Specifies the user for database connection.
     */
    @NotNull
    String user;
    /**
     * Specifies the password for database connection.
     */
    @NotNull
    String password;
    /**
     * Specifies the driver name for database connection.
     */
    String driverName;
}
