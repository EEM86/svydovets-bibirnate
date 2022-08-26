package com.svydovets.bibirnate.demo;

import com.svydovets.bibirnate.demo.entity.Person;
import com.svydovets.bibirnate.session.Session;
import com.svydovets.bibirnate.session.SessionFactory;
import com.svydovets.bibirnate.session.impl.SessionFactoryImpl;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.Optional;

public class DemoApp {
    public static void main(String[] args) {
        initDB();

        SessionFactory sessionFactory = new SessionFactoryImpl(initializeDataSource());
        Session session = sessionFactory.openSession();

        Optional<Person> person = session.findById(22, Person.class);
        person.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));

    }

    @SneakyThrows
    private static DataSource initializeDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres?");
        return dataSource;
    }

    private static void initDB() {
        //todo: init db
    }
}
