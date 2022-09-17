package com.svydovets.bibirnate.demo;

import java.util.Locale;
import java.util.Optional;

import com.svydovets.bibirnate.configuration.context.PersistenceContextProvider;
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

    @SneakyThrows
    public static void main(String[] args) {
        SessionFactory sessionFactory = getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            final Person person1 = new Person();
            person1.setEmail("asda@gmail.com");
            session.persist(person1);
//            var query  = "insert into persons (first_name, last_name, email, age) values (?, ?, ?, ?);";
//            for (int i = 0; i < 1; i++) {
//                Query typedQuery = session.createTypedQuery(query, Person.class);
//                insertIntoPersons(typedQuery);
//                //typedQuery.execute();
//            }

            TransactionManager transactionManager = session.getTransactionManager();
            try {
                transactionManager.begin();

                SqlGenerator.generateInsertQueriesToNotes(1).forEach(noteQuery -> {
                    Query typedQuery = session.createTypedQuery(noteQuery, Note.class);
                    typedQuery.execute();
                });

                transactionManager.commit();
            } catch (Exception ex) {
                transactionManager.rollback();
            }

            Query typedQuery = session.createTypedQuery("select * from persons where first_name like ?", Person.class);
            typedQuery.addParameter("P%");
            Person firstResult = (Person) typedQuery.getFirstResult();
            System.out.println(firstResult.toString());
            //will be extracted from cache
            firstResult = (Person) typedQuery.getFirstResult();
            System.out.println(firstResult.toString());

            typedQuery = session.createTypedQuery("select * from persons", Person.class);
            System.out.println(typedQuery.getResultList().size());
            //will be extracted from cache
            System.out.println(typedQuery.getResultList().size());

            Person person = session.findById(2, Person.class);
            Optional.ofNullable(person)
              .ifPresentOrElse(System.out::println,
                () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));
            //session.remove(person);

            Optional.ofNullable(person)
              .ifPresentOrElse(System.out::println,
                () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));

            Note note = session.findById(2, Note.class);

            Optional.ofNullable(note)
              .ifPresentOrElse(System.out::println,
                () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));

        }
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

        var sessionFactoryBuilder = new DefaultSessionFactoryBuilderImpl()
          .withSqlQueriesLoggingEnabled(sqlLoggingProperties)
          .withDatabaseConnection(dbProperties);

        return PersistenceContextProvider.createSessionFactory(sessionFactoryBuilder);
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
