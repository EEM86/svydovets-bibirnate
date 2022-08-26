package com.svydovets.bibirnate.session.impl;

import javax.sql.DataSource;

import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionFactoryImpl implements SessionFactory {
    private final DataSource dataSource;

    @Override
    public Session openSession() {
        return new SessionImpl(dataSource);
    }
}
