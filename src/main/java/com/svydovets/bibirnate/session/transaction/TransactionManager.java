package com.svydovets.bibirnate.session.transaction;

import com.svydovets.bibirnate.exceptions.TransactionManagerException;

public interface TransactionManager {

    void begin() throws TransactionManagerException;

    void commit() throws TransactionManagerException;

    void rollback() throws TransactionManagerException;
}
