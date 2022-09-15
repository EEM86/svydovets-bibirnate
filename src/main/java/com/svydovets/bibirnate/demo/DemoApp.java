package com.svydovets.bibirnate.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.svydovets.bibirnate.configuration.context.PersistenceContextProvider;
import com.svydovets.bibirnate.configuration.properties.DatabaseProperties;
import com.svydovets.bibirnate.configuration.properties.LoggingProperties;
import com.svydovets.bibirnate.demo.entity.Note;
import com.svydovets.bibirnate.demo.entity.NoteDescription;
import com.svydovets.bibirnate.demo.entity.Person;
import com.svydovets.bibirnate.demo.entity.SomeToOneRelation;
import com.svydovets.bibirnate.demo.entity.Test;
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
//            var query  = "insert into persons (first_name, last_name, email, age) values (?, ?, ?, ?);";
//            for (int i = 0; i < 1; i++) {
//                Query typedQuery = session.createTypedQuery(query, Person.class);
//                insertIntoPersons(typedQuery);
//                //typedQuery.execute();
//            }
//
//            TransactionManager transactionManager = session.getTransactionManager();
//            transactionManager.begin();
//
//            SqlGenerator.generateInsertQueriesToNotes(100).forEach(noteQuery -> {
//                Query typedQuery = session.createTypedQuery(noteQuery, Note.class);
//                typedQuery.execute();
//            });
//
//            transactionManager.rollback();
//
//            Query typedQuery = session.createTypedQuery("select * from persons where first_name like ?", Person.class);
//            typedQuery.addParameter("P%");
//            Person firstResult = (Person) typedQuery.getFirstResult();
//            System.out.println(firstResult.toString());
//            //cached
//            firstResult = (Person) typedQuery.getFirstResult();
//            System.out.println(firstResult.toString());
//
//            typedQuery = session.createTypedQuery("select * from persons", Person.class);
//            System.out.println(typedQuery.getResultList().size());
//            //will be extracted from cache
//            System.out.println(typedQuery.getResultList().size());
//
//            Person person = session.findById(10, Person.class);
//            Optional.ofNullable(person)
//              .ifPresentOrElse(System.out::println,
//                () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));
//            //will be extracted from cache
//            person = session.findById(2, Person.class);
//            Optional.ofNullable(person)
//              .ifPresentOrElse(System.out::println,
//                () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));
//
//            Note note = session.findById(7, Note.class);
//
//            Optional.ofNullable(note)
//              .ifPresentOrElse(System.out::println,
//                () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));
//
//            session.remove(person);
//
//            Optional.ofNullable(person)
//              .ifPresentOrElse(System.out::println,
//                () -> System.out.println("There is no such object  ¯\\_(ツ)_/¯"));

            Test t = new Test();
            t.setTest("ttt");
            t.setId(1L);

            Person person = new Person();
            person.setId(1L);
            person.setFirstName("v");
            person.setLastName("p");
            //            person.setTest(t);
            Note n = new Note();
            n.setId(1L);
            n.setNote("test");
            n.setPerson(person);
            Note n2 = new Note();
            n2.setId(2L);
            n2.setNote("test2");
            n2.setPerson(person);
            List<Note> notes = new ArrayList<>();
            notes.add(n);
            notes.add(n2);
            person.setNotes(notes);

            //            DELETE FROM notes WHERE person_id = 1
            //            DELETE FROM persons WHERE id = 1
//            session.remove(person);

            NoteDescription noteDescription = new NoteDescription();
            noteDescription.setId(1L);
            noteDescription.setDescription("note Descr");
            noteDescription.setNote(n);
            NoteDescription noteDescription2 = new NoteDescription();
            noteDescription2.setId(2L);
            noteDescription2.setDescription("note Descr 2");
            noteDescription2.setNote(n);
            NoteDescription noteDescription3 = new NoteDescription();
            noteDescription3.setId(3L);
            noteDescription3.setDescription("note Descr 3");
            noteDescription3.setNote(n2);
            NoteDescription noteDescription4 = new NoteDescription();
            noteDescription4.setId(4L);
            noteDescription4.setDescription("note Descr 4");
            noteDescription4.setNote(n2);

            List<NoteDescription> nd1 = new ArrayList<>(2);
            nd1.add(noteDescription);
            nd1.add(noteDescription2);
            n.setDescriptions(nd1);
            List<NoteDescription> nd2 = new ArrayList<>(2);
            nd2.add(noteDescription3);
            nd2.add(noteDescription4);
            n2.setDescriptions(nd2);

            //            DELETE FROM note_descriptions WHERE note_id = 2
            //            DELETE FROM note_descriptions WHERE note_id = 1
            //            DELETE FROM notes WHERE person_id = 1
            //            DELETE FROM persons WHERE id = 1
//                        session.remove(person);


            //            Note note = new Note();
            //            note.setId(12L);
            //            note.setNote("DSCR");
            //
            Test tst = new Test();
            tst.setId(13L);
            tst.setTest("tttt");
//                        tst.setNote(note);
            tst.setPersonList(Collections.singletonList(person));
            //            person.setTest(tst);

            //            note.setTest(tst);
            //            DELETE FROM note_descriptions WHERE note_id = 2
            //            DELETE FROM note_descriptions WHERE note_id = 1
            //            DELETE FROM notes WHERE person_id = 1
            //            DELETE FROM persons WHERE id = 1
            //            DELETE FROM t WHERE id = 13
//                        session.remove(person);

            SomeToOneRelation tst2 = new SomeToOneRelation();
            tst2.setId(1444L);
            tst2.setText("text");
            tst2.setNote(n);
            n.setRelation(tst2);

//                        session.remove(person);

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
