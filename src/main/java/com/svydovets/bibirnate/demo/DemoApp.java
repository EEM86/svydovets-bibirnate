package com.svydovets.bibirnate.demo;

import java.util.Optional;

import com.svydovets.bibirnate.configuration.context.PersistenceContextProvider;
import com.svydovets.bibirnate.configuration.properties.DatabaseProperties;
import com.svydovets.bibirnate.demo.entity.Person;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;
import com.svydovets.bibirnate.session.impl.builder.DefaultSessionFactoryBuilderImpl;

import lombok.SneakyThrows;

public class DemoApp {

    @SneakyThrows
    public static void main(String[] args) {
        initDB();

        SessionFactory sessionFactory = getSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            Person person = session.findById(22, Person.class);
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
