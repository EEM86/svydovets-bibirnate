package com.svydovets.bibirnate.demo;

import java.util.Optional;
import javax.sql.DataSource;

import com.svydovets.bibirnate.configuration.YamlConfigurationPropertiesReaderImpl;
import com.svydovets.bibirnate.configuration.context.DefaultYamlPersistenceContextBuilderImpl;
import com.svydovets.bibirnate.configuration.context.PersistenceContextProvider;
import com.svydovets.bibirnate.demo.entity.Person;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;
import com.svydovets.bibirnate.utils.HikariConfigUtils;
import com.zaxxer.hikari.HikariDataSource;

import lombok.SneakyThrows;

public class DemoApp {

    //TODO:
    // Describe possible solution
    // Check Abstract Factory, Factory patterns
    //
    //TODO:
    // Problem
    // Need to create persistent contex
    // Provide PersistentContextFactory to provide persistentContext with Default and custom implementations
    // PersistentContextFactory should configure all db properties, logging, caching
    //

    //TODO:
    // Vizuailize current implementation with
    // getSession -> full flow


    @SneakyThrows
    public static void main(String[] args) {
        initDB();



        SessionFactory sessionFactory = PersistenceContextProvider.initializePersistenceContext(
          new DefaultYamlPersistenceContextBuilderImpl()
                        .fromFile("persistence-example.yaml"))
                .getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Person person = session.findById(22, Person.class);
            Optional.ofNullable(person)
                    .ifPresentOrElse(System.out::println,
                      () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));
        }

    }

    @SneakyThrows
    private static DataSource initializeDataSource() {
        var configurationProperties = new YamlConfigurationPropertiesReaderImpl()
                .readProperties("persistence-example.yaml");
        var hikariConfig = HikariConfigUtils.createHikariConfig(configurationProperties);

        return new HikariDataSource(hikariConfig);
    }

    private static void initDB() {
        //todo: init db
    }
}
