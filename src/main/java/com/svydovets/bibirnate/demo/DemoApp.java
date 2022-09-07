package com.svydovets.bibirnate.demo;

import java.util.Optional;
import javax.sql.DataSource;

import com.svydovets.bibirnate.configuration.YamlConfigurationPropertiesReaderImpl;
import com.svydovets.bibirnate.demo.entity.Person;
import com.svydovets.bibirnate.session.impl.SessionFactoryImpl;
import com.svydovets.bibirnate.utils.HikariConfigUtils;
import com.zaxxer.hikari.HikariDataSource;

import lombok.SneakyThrows;

public class DemoApp {
    public static void main(String[] args) {
        initDB();

        var sessionFactory = new SessionFactoryImpl(initializeDataSource("persistence-example.yaml"));
        var session = sessionFactory.openSession();

        Person person = session.findById(22, Person.class);

        Optional.ofNullable(person)
          .ifPresentOrElse(System.out::println,
                () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));
    }

    @SneakyThrows
    private static DataSource initializeDataSource(String properties) {
        var configurationProperties = new YamlConfigurationPropertiesReaderImpl()
                .readProperties(properties);
        var hikariConfig = HikariConfigUtils.createHikariConfig(configurationProperties);

        return new HikariDataSource(hikariConfig);
    }

    private static void initDB() {
        //todo: init db
    }
}
