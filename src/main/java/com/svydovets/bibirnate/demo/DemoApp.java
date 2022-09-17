package com.svydovets.bibirnate.demo;

import java.util.Locale;
import java.util.Optional;

import com.svydovets.bibirnate.configuration.context.PersistenceContextProvider;
import com.svydovets.bibirnate.configuration.properties.ConnectionPoolProperties;
import com.svydovets.bibirnate.configuration.properties.DatabaseProperties;
import com.svydovets.bibirnate.configuration.properties.LoggingProperties;
import com.svydovets.bibirnate.demo.entity.Note;
import com.svydovets.bibirnate.demo.entity.Person;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;
import com.svydovets.bibirnate.session.impl.builder.DefaultSessionFactoryBuilderImpl;
import com.svydovets.bibirnate.session.query.Query;
import com.svydovets.bibirnate.session.transaction.TransactionManager;

import lombok.SneakyThrows;

public class DemoApp {

    private static SessionFactory getSessionFactory() {
        var sqlLoggingProperties = new LoggingProperties();
        sqlLoggingProperties.setEnabled(true);

        var dbProperties = DatabaseProperties.builder()
                .url("jdbc:postgresql://rds-postgres-svydovets.c3bxmbbb5a4p"
                        + ".eu-central-1.rds.amazonaws.com:5432/svydovetsDB")
                .user("masterSvydovets")
                .password("demo4bibernate")
                .driverName("org.postgresql.Driver")
                .build();

        //TODO: provide actual DB schema:
        // for goverla team -> goverla
        // for petros team -> petros
        // for breskul team -> breskul
        // for blyznytsia team -> blyznytsia
        var connectionPoolProperties = new ConnectionPoolProperties();
        connectionPoolProperties.setSchema("public");

        var sessionFactoryBuilder = new DefaultSessionFactoryBuilderImpl()
                .withSqlQueriesLoggingEnabled(sqlLoggingProperties)
                .withDatabaseConnection(dbProperties)
                .withConnectionPool(connectionPoolProperties);

        return PersistenceContextProvider.createSessionFactory(sessionFactoryBuilder);
    }

    @SneakyThrows
    public static void main(String[] args) {
    //        var person = new Person();
    //        person.setEmail("34tgdfg@gmail.com");
    //        person.setFirstName("34tgdfg@gmail.com");
    //        person.setLastName("34tgdfg@gmail.com");
    //        SessionFactory sessionFactory = getSessionFactory();
    //        try (Session session = sessionFactory.openSession()) {
    //            TransactionManager transactionManager = session.getTransactionManager();
    //
    //            Query typedQuery = session.createTypedQuery("select * from persons where first_name like ?",
    //            Person.class);
    //            typedQuery.addParameter("P%");
    //            Person firstResult = (Person) typedQuery.getFirstResult();
    //            System.out.println(firstResult.toString());
    //            //will be extracted from cache
    //            firstResult = (Person) typedQuery.getFirstResult();
    //            System.out.println(firstResult.toString());
    //
    //            typedQuery = session.createTypedQuery("select * from persons", Person.class);
    //            System.out.println(typedQuery.getResultList().size());
    //            //will be extracted from cache
    //            System.out.println(typedQuery.getResultList().size());
    //
    //            Person person1 = session.findById(2, Person.class);
    //            Optional.ofNullable(person1)
    //                    .ifPresentOrElse(System.out::println,
    //                            () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));
    //            session.remove(person1);
    //
    //            Optional.ofNullable(person1)
    //                    .ifPresentOrElse(System.out::println,
    //                            () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));
    //
    //            Note note = session.findById(2, Note.class);
    //
    //            Optional.ofNullable(note)
    //                    .ifPresentOrElse(System.out::println,
    //                            () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));
    //            try {
    //                transactionManager.begin();
    //
    //                session.persist(person);
    //
    //                transactionManager.commit();
    //            } catch (Exception ex) {
    //                transactionManager.rollback();
    //            }
    //
    //            TransactionManager transactionManager1 = session.getTransactionManager();
    //            try {
    //                transactionManager1.begin();
    //
    //                SqlGenerator.generateInsertQueriesToNotes(1).forEach(noteQuery -> {
    //                    Query typedQuery1 = session.createTypedQuery(noteQuery, Note.class);
    //                    typedQuery1.execute();
    //                });
    //
    //                transactionManager1.commit();
    //            } catch (Exception ex) {
    //                transactionManager1.rollback();
    //            }
    //
    //        } catch (Exception ex) {
    //            // do smth
    //        }
    }

    private static void insertIntoPersons(Query typedQuery) {
        String randomFirstName = SqlGenerator.getRandomFirstName();
        String randomLastName = SqlGenerator.getRandomLastName();
        String emailEnd = SqlGenerator.getRandomEmailEnd();
        int age = SqlGenerator.getRandomNumberUsingInts(15, 85);
        int randomInt = SqlGenerator.getRandomNumberUsingInts(0, 99);
        var email = String.format("%s%s.%s_%s%s", randomFirstName.toLowerCase(Locale.ROOT), randomInt,
                randomLastName.toLowerCase(), age, emailEnd);

        typedQuery.addParameter(randomFirstName);
        typedQuery.addParameter(randomLastName);
        typedQuery.addParameter(email);
        typedQuery.addParameter(age);
    }
}
