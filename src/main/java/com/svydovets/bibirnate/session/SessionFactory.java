package com.svydovets.bibirnate.session;

import javax.sql.DataSource;

public interface SessionFactory {
    Session createSession(DataSource dataSource);
}
