package com.svydovets.bibirnate.configuration.properties;

import com.svydovets.bibirnate.configuration.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
