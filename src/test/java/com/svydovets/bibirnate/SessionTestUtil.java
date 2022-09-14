package com.svydovets.bibirnate;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Utility class for mock and other configs for Session in test env.
 */
@UtilityClass
public class SessionTestUtil {

  private static final String DEFAULT_URL = "jdbc:postgresql://localhost:9993/postgres";

  public static void mockConnectionMetadata(Connection connection) throws SQLException {
    mockConnectionMetadata(connection, DEFAULT_URL);
  }

  public static void mockConnectionMetadata(Connection connection, String url) throws SQLException {
    when(connection.getMetaData()).thenReturn(mock(DatabaseMetaData.class));
    when(connection.getMetaData().getURL()).thenReturn(url);
  }
}
