package com.svydovets.bibirnate.session.impl;

import static com.svydovets.bibirnate.session.impl.JdbcEntityDaoFactory.createJdbcEntityDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.svydovets.bibirnate.exceptions.JdbcException;
import com.svydovets.bibirnate.session.Session;

public class SessionImpl implements Session {
    private final JdbcEntityDao jdbcEntityDao;

    private final Connection connection;

    public SessionImpl(Connection connection) {
        this.jdbcEntityDao = createJdbcEntityDao(connection);
        this.connection = connection;
    }

    @Override
    public <T> Optional<T> findById(Object id, Class<T> type) {
        return jdbcEntityDao.findById(id, type);
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new JdbcException(ex.getMessage(), ex);
        }
    }
}
