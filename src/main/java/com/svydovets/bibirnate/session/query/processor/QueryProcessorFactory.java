package com.svydovets.bibirnate.session.query.processor;

import java.sql.Connection;

import com.svydovets.bibirnate.exceptions.UnsupportedPersistentOperationException;
import com.svydovets.bibirnate.logs.SqlLogger;
import com.svydovets.bibirnate.session.query.CrudOperation;

public class QueryProcessorFactory {

    private QueryProcessorFactory() {
    }

    public static QueryProcessor defineQueryProcessor(CrudOperation operation, Object persistentObject,
                                                      Connection connection, SqlLogger sqlLogger) {

        return switch (operation) {
            case DELETE -> new DeleteQueryProcessor(persistentObject, connection, sqlLogger);
            case UPDATE -> new UpdateQueryProcessor(persistentObject, connection, sqlLogger);
            default -> throw new UnsupportedPersistentOperationException(String
                    .format("Could not define query processor for operation type: %s", operation));
        };

    }

}
