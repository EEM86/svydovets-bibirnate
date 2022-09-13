package com.svydovets.bibirnate.session.query.processor;

import static com.svydovets.bibirnate.utils.CommonConstants.INVALID_ID_PERSON;
import static com.svydovets.bibirnate.utils.CommonConstants.INVALID_PERSON;
import static com.svydovets.bibirnate.utils.CommonConstants.PERSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.svydovets.bibirnate.exceptions.EntityValidationException;
import com.svydovets.bibirnate.exceptions.PersistenceException;
import com.svydovets.bibirnate.logs.SqlLogger;

import lombok.SneakyThrows;

class DeleteQueryProcessorTest {

    private static QueryProcessor PROCESSOR;
    private static Connection CONNECTION;
    private static final String INVALID_ENTITY_ID_MSG =
      "Entity class should have id field annotated with @Id annotation";
    private static final String INVALID_ENTITY_MSG = "Persistent Class should be annotated with @Entity annotation";
    private static final String PERSISTENCE_EXCEPTION_MSG = "Could not Execute DELETE statement";


    @BeforeAll
    static void init() {
        CONNECTION = Mockito.mock(Connection.class);
        PROCESSOR = new DeleteQueryProcessor(PERSON, CONNECTION, new SqlLogger(false));
    }

    @SneakyThrows
    @Test
    void generateQuery() {
        Connection connection = Mockito.mock(Connection.class);
        var expected_sql_query = "DELETE FROM persons WHERE id = 1";
        var persistent_fields_count = 3;
        var table_name = "persons";
        var processor = new DeleteQueryProcessor(PERSON, connection, new SqlLogger(false));
        var deleteQuery = processor.generateQuery();
        assertEquals(processor.getPersistentObject(), PERSON);
        assertEquals(table_name, processor.getTableName());
        assertEquals(processor.getConnection(), connection);
        assertEquals(processor.getId().get(PERSON), PERSON.getId());
        assertEquals(persistent_fields_count, processor.getEntityFields().size());
//        assertNull(processor.getParentId());
        assertEquals(expected_sql_query, deleteQuery);
        assertFalse(processor.hasToOneRelations());
        assertFalse(processor.hasToManyRelations());
        assertFalse(processor.hasParent());
    }

    @Test
    void generateQueryValidationNoEntityAnnotationException() {
        Exception exception = assertThrows(EntityValidationException.class,
          () -> new DeleteQueryProcessor(INVALID_PERSON, CONNECTION, new SqlLogger(false)));
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(INVALID_ENTITY_MSG));
    }

    @Test
    void generateQueryValidationNoIdField() {
        Exception exception = assertThrows(EntityValidationException.class,
          () -> new DeleteQueryProcessor(INVALID_ID_PERSON, CONNECTION, new SqlLogger(false)));
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(INVALID_ENTITY_ID_MSG));
    }

    @SneakyThrows
    @Test
    void execute() {
        var processor = new DeleteQueryProcessor(PERSON, CONNECTION, new SqlLogger(false));
        var stmt = Mockito.mock(Statement.class);
        when(CONNECTION.createStatement()).thenReturn(stmt);
        processor.execute();
        verify(stmt).execute(anyString());
    }

    @Test
    void executeThrowsPersistentException() throws SQLException {
        var stmt = Mockito.mock(Statement.class);
        when(CONNECTION.createStatement()).thenReturn(stmt);
        when(stmt.execute(anyString())).thenThrow(SQLException.class);
        Exception exception = assertThrows(PersistenceException.class, PROCESSOR::execute);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(PERSISTENCE_EXCEPTION_MSG));

    }
}