package com.svydovets.bibirnate.demo;

import java.util.Optional;

import com.svydovets.bibirnate.configuration.context.PersistenceContextProvider;
import com.svydovets.bibirnate.configuration.properties.DatabaseProperties;
import com.svydovets.bibirnate.demo.entity.Person;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;
import com.svydovets.bibirnate.session.impl.builder.DefaultSessionFactoryBuilderImpl;
import com.svydovets.bibirnate.session.query.Query;

import lombok.SneakyThrows;

public class DemoApp {

    @SneakyThrows
    public static void main(String[] args) {
        initDB();

        SessionFactory sessionFactory = getSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            Query typedQuery = session.createTypedQuery("select * from persons where first_name like ?", Person.class);
            typedQuery.addParameter("V%");
            Person singleResult = (Person) typedQuery.getSingleResult();
            System.out.println(singleResult.toString());

            typedQuery = session.createTypedQuery(
              "select * from persons inner join notes on persons.id = notes.person_id;", Person.class);
            System.out.println(typedQuery.getResultList());
            System.out.println(typedQuery.getResultSet());

            typedQuery = session.createTypedQuery("update persons set first_name = ? where id = ?", Person.class);
            typedQuery.addParameter("Test");
            typedQuery.addParameter(1);
            typedQuery.execute();

            typedQuery = session.createTypedQuery("select * from persons", Person.class);
            System.out.println(typedQuery.getResultList());
            System.out.println(typedQuery.getResultSet());

            Person person = session.findById(2, Person.class);

            Optional.ofNullable(person)
              .ifPresentOrElse(System.out::println,
                () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));
        }
    }

    private static SessionFactory getSessionFactory() {
        return PersistenceContextProvider.createSessionFactory(new DefaultSessionFactoryBuilderImpl()
          .withDatabaseConnection(DatabaseProperties.builder()
            .url("jdbc:postgresql://localhost:5432/bibernate")
            .user("maingroon")
            .password("password")
            .driverName("org.postgresql.Driver")
            .build()));
    }

    private static void initDB() {
        //todo: init db
    }
}
