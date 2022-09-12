package com.svydovets.bibirnate.session.transaction;

import com.svydovets.bibirnate.exceptions.TransactionManagerException;

import java.sql.Connection;
import java.sql.SQLException;

import static com.svydovets.bibirnate.session.transaction.TransactionStatus.*;

public class TransactionManagerImpl implements TransactionManager {

    private final Connection connection;
    private TransactionStatus status = TransactionStatus.NONE;

    public TransactionManagerImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void begin() throws TransactionManagerException {
        if (status != NONE) {
            throw new TransactionManagerException("Transaction already started.");
        }
        try {
            connection.setAutoCommit(false);
            status = BEGIN;
        } catch (SQLException e) {
            throw new TransactionManagerException(e);
        }
    }

    @Override
    public void commit() throws TransactionManagerException {
        if (status != BEGIN) {
            throw new TransactionManagerException("Transaction not started or already finished.");
        }
        try {
            connection.commit();
            connection.close();
            status = COMMIT;
        } catch (SQLException e) {
            throw new TransactionManagerException(e);
        }
    }

    @Override
    public void rollback() throws TransactionManagerException {
        if (status != BEGIN) {
            throw new TransactionManagerException("Transaction not started or already finished.");
        }
        try {
            connection.rollback();
            connection.close();
            status = ROLLBACK;
        } catch (SQLException e) {
            throw new TransactionManagerException(e);
        }
    }


}
