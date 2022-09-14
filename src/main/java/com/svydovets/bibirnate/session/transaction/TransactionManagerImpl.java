package com.svydovets.bibirnate.session.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import com.svydovets.bibirnate.exceptions.TransactionManagerException;

public class TransactionManagerImpl implements TransactionManager {

    private final Connection connection;
    private boolean transactionStarted = false;

    public TransactionManagerImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void begin() throws TransactionManagerException {
        if (transactionStarted) {
            throw new TransactionManagerException("Transaction already started.");
        }
        try {
            connection.setAutoCommit(false);
            transactionStarted = true;
        } catch (SQLException exception) {
            throw new TransactionManagerException(exception);
        }
    }

    @Override
    public void commit() throws TransactionManagerException {
        if (!transactionStarted) {
            throw new TransactionManagerException("Transaction not started or already finished.");
        }
        try {
            connection.commit();
            connection.close();
            transactionStarted = false;
        } catch (SQLException exception) {
            throw new TransactionManagerException(exception);
        }
    }

    @Override
    public void rollback() throws TransactionManagerException {
        if (!transactionStarted) {
            throw new TransactionManagerException("Transaction not started or already finished.");
        }
        try {
            connection.rollback();
            connection.close();
            transactionStarted = false;
        } catch (SQLException exception) {
            throw new TransactionManagerException(exception);
        }
    }


}
