package com.svydovets.bibirnate.session.query.processor;

import java.sql.Connection;

import com.svydovets.bibirnate.exceptions.UnsupportedPersistentOperationException;
import com.svydovets.bibirnate.session.query.CrudOperation;

public class QueryProcessorFactory {

    private QueryProcessorFactory() {
    }

    public static QueryProcessor defineQueryProcessor(CrudOperation operation, Object persistentObject,
                                                      Connection connection) {

        switch (operation) {
            case DELETE:
                return new DeleteQueryProcessor(persistentObject, connection);
            default:
                throw new UnsupportedPersistentOperationException(String
                  .format("Could not define query processor for operation type: %s", operation));

        }

    }

}