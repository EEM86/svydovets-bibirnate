package com.svydovets.bibirnate.session.query.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Connection;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.svydovets.bibirnate.entities.PersonSimpleEntity;

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
    }

    @Test
    void execute() {
    }
}