package com.svydovets.bibirnate.session.query.processor;

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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.svydovets.bibirnate.entities.PersonSimpleEntity;
import com.svydovets.bibirnate.entities.PersonSimpleInvalidEntity;
import com.svydovets.bibirnate.entities.PersonSimpleInvalidEntityNoId;
import com.svydovets.bibirnate.exceptions.EntityValidationException;
import com.svydovets.bibirnate.exceptions.PersistenceException;

import lombok.SneakyThrows;

class DeleteQueryProcessorTest {

    @SneakyThrows
    @Test
    void generateQuery() {
        Connection connection = Mockito.mock(Connection.class);
        var expected_sql_query = "DELETE FROM persons WHERE id = 1";
        var persistent_fields_count = 3;
        var table_name = "persons";
        var person = new PersonSimpleEntity(1L, "name", "last_name", "blindValue");
        var processor = new DeleteQueryProcessor(person, connection);
        var deleteQuery = processor.generateQuery();
        assertEquals(processor.getPersistentObject(), person);
        assertEquals(table_name, processor.getTableName());
        assertEquals(processor.getConnection(), connection);
        assertEquals(processor.getId().get(person), person.getId());
        assertEquals(persistent_fields_count, processor.getEntityFields().size());
        assertNull(processor.getParentId());
        assertEquals(expected_sql_query, deleteQuery);
        assertFalse(processor.hasToOneRelations());
        assertFalse(processor.hasToManyRelations());
        assertFalse(processor.hasParent());
    }

    @Test
    void generateQueryValidationNoEntityAnnotationException() {
        Connection connection = Mockito.mock(Connection.class);
        var person = new PersonSimpleInvalidEntity(1L, "name", "last_name", "blindValue");
        Exception exception = assertThrows(EntityValidationException.class,
          () -> new DeleteQueryProcessor(person, connection));
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Persistent Class should be annotated with @Entity annotation"));
    }

    @Test
    void generateQueryValidatioNoIdField() {
        Connection connection = Mockito.mock(Connection.class);
        var person = new PersonSimpleInvalidEntityNoId(1L, "name", "last_name", "blindValue");
        Exception exception = assertThrows(EntityValidationException.class,
          () -> new DeleteQueryProcessor(person, connection));
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Entity class should have id field annotated with @Id annotation"));
    }

    @SneakyThrows
    @Test
    void execute() {
        Connection connection = Mockito.mock(Connection.class);
        var person = new PersonSimpleEntity(1L, "name", "last_name", "blindValue");
        var processor = new DeleteQueryProcessor(person, connection);
        var stmt = Mockito.mock(Statement.class);
        when(connection.createStatement()).thenReturn(stmt);
        processor.execute();
        verify(stmt).execute(anyString());
    }

    @Test
    void executeThrowsPersistentException() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        var person = new PersonSimpleEntity(1L, "name", "last_name", "blindValue");
        var processor = new DeleteQueryProcessor(person, connection);
        var stmt = Mockito.mock(Statement.class);
        when(connection.createStatement()).thenReturn(stmt);
        when(stmt.execute(anyString())).thenThrow(SQLException.class);
        Exception exception = assertThrows(PersistenceException.class, processor::execute);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Could not Execute DELETE statement"));

    }
}