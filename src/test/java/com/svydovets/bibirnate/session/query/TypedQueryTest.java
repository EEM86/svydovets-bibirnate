package com.svydovets.bibirnate.session.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.svydovets.bibirnate.configuration.context.PersistenceContextProvider;
import com.svydovets.bibirnate.configuration.properties.ConnectionPoolProperties;
import com.svydovets.bibirnate.configuration.properties.DatabaseProperties;
import com.svydovets.bibirnate.configuration.properties.LoggingProperties;
import com.svydovets.bibirnate.entity.Tests;
import com.svydovets.bibirnate.exceptions.NoResultException;
import com.svydovets.bibirnate.exceptions.NoUniqueResultException;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;
import com.svydovets.bibirnate.session.impl.builder.DefaultSessionFactoryBuilderImpl;
import com.svydovets.bibirnate.session.transaction.TransactionManager;

import lombok.SneakyThrows;

public class TypedQueryTest {
    private static SessionFactory sessionFactory;
    private Session session;
    private static Query query;

    @BeforeAll
    static void beforeAll() {
        sessionFactory = getSessionFactory();
    }

    @BeforeEach
    void setUp() {
        session = sessionFactory.openSession();
    }

    @Test
    void getSingleResult_throwsNoResultException() {
        query = session.createTypedQuery("select * from tests where id = ?", Tests.class);
        query.addParameter(1);

        try {
            query.getSingleResult();
        } catch (NoResultException ex) {
            assertEquals(NoResultException.class, ex.getClass());
        }
    }

    @Test
    void getSingleResult_throwsNoUniqueResultException() {
        query = session.createTypedQuery("select * from tests where description like ?", Tests.class);
        query.addParameter("Test%");

        try {
            query.getSingleResult();
        } catch (NoUniqueResultException ex) {
            assertEquals(NoUniqueResultException.class, ex.getClass());
        }
    }

    @Test
    void getSingleResult_returnSingleResult() {
        query = session.createTypedQuery("select * from tests where description = ?", Tests.class);
        query.addParameter("SINGLE_RESULT_TEST!!!");

        assertNotNull(query.getSingleResult());
    }

    @Test
    void getFirstResult_throwsNoResultException() {
        query = session.createTypedQuery("select * from tests where id = ?", Tests.class);
        query.addParameter(1);

        try {
            query.getFirstResult();
        } catch (NoResultException ex) {
            assertEquals(NoResultException.class, ex.getClass());
        }
    }

    @Test
    void getFirstResult_returnFirstResult() {
        query = session.createTypedQuery("select * from tests where description = ?", Tests.class);
        query.addParameter("SINGLE_RESULT_TEST!!!");

        assertNotNull(query.getFirstResult());

        query = session.createTypedQuery("select * from tests where description like ?", Tests.class);
        query.addParameter("Test%");

        assertNotNull(query.getFirstResult());
    }

    @Test
    void getResultList_returnSingleResult() {
        query = session.createTypedQuery("select * from tests", Tests.class);

        assertFalse(query.getResultList().isEmpty());
    }

    @Test
    void getResultSet_returnSingleResult() {
        query = session.createTypedQuery("select * from tests", Tests.class);

        assertFalse(query.getResultSet().isEmpty());
    }

    @Test
    @SneakyThrows
    void execute_insertIntoDB() {
        TransactionManager transactionManager = session.getTransactionManager();
        transactionManager.begin();

        query = session.createTypedQuery("select * from tests where description = ?;", Tests.class);
        query.addParameter("NEW_TEST_PARAM");
        assertTrue(query.getResultSet().isEmpty());

        query = session.createTypedQuery("insert into tests (description) values (?);", Tests.class);
        query.addParameter("NEW_TEST_PARAM");
        query.execute();

        query = session.createTypedQuery("select * from tests where description = ?;", Tests.class);
        query.addParameter("NEW_TEST_PARAM");
        assertFalse(query.getResultSet().isEmpty());

        transactionManager.rollback();
    }

    @Test
    @SneakyThrows
    void execute_updateIntoDB() {
        TransactionManager transactionManager = session.getTransactionManager();
        transactionManager.begin();

        query = session.createTypedQuery("select * from tests where description = ?;", Tests.class);
        query.addParameter("FOR_UPDATE!!!");
        assertFalse(query.getResultSet().isEmpty());

        query = session.createTypedQuery("insert into tests (description) values (?);", Tests.class);
        query.addParameter("NEW_TEST_PARAM");
        query.execute();

        query = session.createTypedQuery("select * from tests where description = ?;", Tests.class);
        query.addParameter("NEW_TEST_PARAM");
        assertFalse(query.getResultSet().isEmpty());

        transactionManager.rollback();
    }

    @Test
    @SneakyThrows
    void execute_deleteIntoDB() {
        TransactionManager transactionManager = session.getTransactionManager();
        transactionManager.begin();

        query = session.createTypedQuery("select * from tests where description = ?;", Tests.class);
        query.addParameter("FOR_UPDATE!!!");
        assertFalse(query.getResultSet().isEmpty());

        query = session.createTypedQuery("delete from tests where description = ?;", Tests.class);
        query.addParameter("FOR_DELETE!!!");
        query.execute();

        query = session.createTypedQuery("select * from tests where description = ?;", Tests.class);
        query.addParameter("FOR_DELETE!!!");
        assertTrue(query.getResultSet().isEmpty());

        transactionManager.rollback();
    }


    private static SessionFactory getSessionFactory() {
        var sqlLoggingProperties = new LoggingProperties();
        sqlLoggingProperties.setEnabled(true);

        var dbProperties = DatabaseProperties.builder()
          .url("jdbc:postgresql://rds-postgres-svydovets.c3bxmbbb5a4p.eu-central-1.rds.amazonaws.com:5432/svydovetsDB")
          .user("masterSvydovets")
          .password("demo4bibernate")
          .driverName("org.postgresql.Driver")
          .build();

        var connectionPoolProperties = new ConnectionPoolProperties();
        connectionPoolProperties.setSchema("unit_test");

        var sessionFactoryBuilder = new DefaultSessionFactoryBuilderImpl()
          .withSqlQueriesLoggingEnabled(sqlLoggingProperties)
          .withDatabaseConnection(dbProperties)
          .withConnectionPool(connectionPoolProperties);

        return PersistenceContextProvider.createSessionFactory(sessionFactoryBuilder);
    }

}
