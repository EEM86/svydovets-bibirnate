package com.svydovets.bibirnate.session.impl;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionFactoryImpl implements SessionFactory {
    private final DataSource dataSource;

    @Override
    public Session openSession() {
        try {
            return new SessionImpl(dataSource.getConnection());
        } catch (SQLException ex) {
            throw new JdbcException(ex.getMessage(), ex);
        }
    }
}
