package com.svydovets.bibirnate.session.query.processor;

import static com.svydovets.bibirnate.utils.CommonConstants.PERSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.sql.Connection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.svydovets.bibirnate.exceptions.UnsupportedPersistentOperationException;
import com.svydovets.bibirnate.logs.SqlLogger;
import com.svydovets.bibirnate.session.query.CrudOperation;

class QueryProcessorFactoryTest {

    @Test
    void defineQueryProcessor() {
        var processor =
          QueryProcessorFactory.defineQueryProcessor(CrudOperation.DELETE, PERSON, mock(Connection.class),
            new SqlLogger(false));
        Assertions.assertEquals(DeleteQueryProcessor.class, processor.getClass());
    }

    @Test
    void defineQueryProcessorUnsupportedOperation() {
        Exception exception = assertThrows(UnsupportedPersistentOperationException.class,
          () -> QueryProcessorFactory.defineQueryProcessor(CrudOperation.READ, PERSON, mock(Connection.class),
            new SqlLogger(false)));
        assertEquals("Could not define query processor for operation type: READ", exception.getMessage());

    }
}