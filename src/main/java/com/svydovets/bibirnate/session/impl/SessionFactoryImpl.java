package com.svydovets.bibirnate.session.impl;

import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class SessionFactoryImpl implements SessionFactory {
    private final DataSource dataSource;
    @Override
    public Session openSession() {
        return new SessionImpl(dataSource);
    }
}
