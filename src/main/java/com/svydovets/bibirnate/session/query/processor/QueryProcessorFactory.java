package com.svydovets.bibirnate.session.query.processor;

import java.sql.Connection;

import com.svydovets.bibirnate.exceptions.UnsupportedPersistentOperationException;
import com.svydovets.bibirnate.logs.SqlLogger;
import com.svydovets.bibirnate.session.query.CrudOperation;

/**
 * Class for creation a query processor based on CRUD operation type.
 */
public class QueryProcessorFactory {

    private QueryProcessorFactory() {
    }

    /**
     * Returns a specific CRUD query processor.
     *
     * @param operation        - CRUD operation type
     * @param persistentObject - Object been handled with CRUD operation
     * @param connection       - database connection
     * @param sqlLogger        - logger
     */
    public static QueryProcessor defineQueryProcessor(CrudOperation operation, Object persistentObject,
                                                      Connection connection, SqlLogger sqlLogger) {

        switch (operation) {
            case DELETE:
                return new DeleteQueryProcessor(persistentObject, connection, sqlLogger);
            case CREATE:
                return new InsertQueryProcessor(persistentObject, connection, sqlLogger);
            default:
                throw new UnsupportedPersistentOperationException(String
                  .format("Could not define query processor for operation type: %s", operation));

        }

    }

}
