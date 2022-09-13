package com.svydovets.bibirnate.session.query.processor;

import static com.svydovets.bibirnate.utils.CommonConstants.PERSON;
import static org.mockito.Mockito.mock;

import java.sql.Connection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class QueryProcessorTest {

    static QueryProcessor processor;

    @BeforeAll
    static void init() {
        var connection = mock(Connection.class);
        processor = new DeleteQueryProcessor(PERSON, connection);
    }

    @Test
    void hasToManyRelations() {
        Assertions.assertFalse(processor.hasToManyRelations());
    }

    @Test
    void hasToOneRelations() {
        Assertions.assertFalse(processor.hasToOneRelations());
    }

    @Test
    void hasParent() {
        Assertions.assertFalse(processor.hasParent());
    }
}