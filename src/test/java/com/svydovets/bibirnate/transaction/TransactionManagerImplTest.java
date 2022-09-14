package com.svydovets.bibirnate.transaction;

import com.svydovets.bibirnate.exceptions.TransactionManagerException;
import com.svydovets.bibirnate.session.transaction.TransactionManagerImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static com.svydovets.bibirnate.SessionTestUtil.mockConnectionMetadata;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TransactionManagerImplTest {

    @Test
    @SneakyThrows
    void testBeginTransaction() {
        var connection = mock(Connection.class);
        mockConnectionMetadata(connection);

        var transactionManager = new TransactionManagerImpl(connection);
        transactionManager.begin();

        var transactionStartedField = TransactionManagerImpl.class.getDeclaredField("transactionStarted");
        transactionStartedField.setAccessible(true);

        assertTrue((boolean) transactionStartedField.get(transactionManager));
    }

    @Test
    @SneakyThrows
    void testAlreadyBeginTransaction() {
        var connection = mock(Connection.class);
        mockConnectionMetadata(connection);

        var transactionManager = new TransactionManagerImpl(connection);

        var transactionStartedField = TransactionManagerImpl.class.getDeclaredField("transactionStarted");
        transactionStartedField.setAccessible(true);
        transactionStartedField.setBoolean(transactionManager, true);

        assertThrows(TransactionManagerException.class, transactionManager::begin);
    }

    @Test
    @SneakyThrows
    void testCommitTransaction() {
        var connection = mock(Connection.class);
        mockConnectionMetadata(connection);

        var transactionManager = new TransactionManagerImpl(connection);
        transactionManager.begin();
        transactionManager.commit();

        var transactionStartedField = TransactionManagerImpl.class.getDeclaredField("transactionStarted");
        transactionStartedField.setAccessible(true);

        assertFalse((boolean) transactionStartedField.get(transactionManager));
    }

    @Test
    @SneakyThrows
    void testCommitTransactionWithoutBegin() {
        var connection = mock(Connection.class);
        mockConnectionMetadata(connection);

        var transactionManager = new TransactionManagerImpl(connection);

        assertThrows(TransactionManagerException.class, transactionManager::commit);
    }

    @Test
    @SneakyThrows
    void testRollbackTransaction() {
        var connection = mock(Connection.class);
        mockConnectionMetadata(connection);

        var transactionManager = new TransactionManagerImpl(connection);
        transactionManager.begin();
        transactionManager.rollback();

        var transactionStartedField = TransactionManagerImpl.class.getDeclaredField("transactionStarted");
        transactionStartedField.setAccessible(true);

        assertFalse((boolean) transactionStartedField.get(transactionManager));
    }

    @Test
    @SneakyThrows
    void testRollbackTransactionWithoutBegin() {
        var connection = mock(Connection.class);
        mockConnectionMetadata(connection);

        var transactionManager = new TransactionManagerImpl(connection);

        assertThrows(TransactionManagerException.class, transactionManager::rollback);
    }
}
