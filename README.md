# Bibernate ORM

### ORM framework made by Svydovets team.

## Configuration

<details>
<summary>Description</summary>

To start using Bibernate ORM client is needed to obtain SessionFactory instance using PersistenceContextProvider
with SessionFactoryBuilder. The configuration can be passed to ORM by YAML-file configuration or using Java-class objects.
Configuration includes database connection properties, connection pool properties, second level cache configuration and 
logging SQL queries.
<br>

#### Properties description:

<details lang="yaml">
<summary>Configurable properties description</summary>

```markdown

Database connection properties

url - specifies DB url. String
user - specifies user for connConfiguration for Bibernate ORM can be provided in 2 ways using  
provide YAML file with configuration properties 
provide configuration through Java POJO classes
ection to DB. String
password - specifies password for connection to DB. String
driverName - specifies DB driver name. String

Connection pool properties:

maxPoolSize - maximum number of connections that will be in the pool. Integer
minIdle: #minimum number of idle connections that will be maintained in the pool. Integer 
maxLifetime: #maximum lifetime of a connection in the pool. Integer
connectionTimeout: #keepalive interval for a connection in the pool. Integer

Second level cache configuration:

enabled - enables/disable second level caching. Boolean, defaultValue =  FALSE
size - max size of the second cache. Integer, default = 0

SQL logging configuration:

enabled - enables/disables logging SQL queries. Boolean, defaultValue = FALSE
```

</details>

<br>

#### Java-code configuration example:
<details lang="en">
<summary>Code snippet</summary>

<details lang="java">
<summary>Minimal configuration</summary>

```java
    var dbProperties = DatabaseProperties.builder()
          .url("jdbc:postgresql://rds-postgres-svydovets.c3bxmbbb5a4p.eu-central-1.rds.amazonaws.com:5432/svydovetsDB")
          .user("masterSvydovets")
          .password("demo4bibernate")
          .driverName("org.postgresql.Driver")
          .build();

    var sessionFactoryBuilder = new DefaultSessionFactoryBuilderImpl()
          .withSqlQueriesLoggingEnabled(sqlLoggingProperties)
          .withDatabaseConnection(dbProperties);
    
    var sessionFactory = PersistenceContextProvider.createSessionFactory(sessionFactoryBuilder);
```

</details>


<details lang="java">
<summary>Configuration with secondLevelCache and SQL logging</summary>

```java

    var dbProperties = DatabaseProperties.builder()
        .url("jdbc:postgresql://rds-postgres-svydovets.c3bxmbbb5a4p.eu-central-1.rds.amazonaws.com:5432/svydovetsDB")
        .user("masterSvydovets")
        .password("demo4bibernate")
        .driverName("org.postgresql.Driver")
        .build();

    var sqlLoggingProperties = new LoggingProperties();
        sqlLoggingProperties.setEnabled(true);

    var cachePropperties = new CacheProperties();
    cachePropperties.setEnabled(true);
    cachePropperties.setSize(200_000);

    var sessionFactoryBuilder = new DefaultSessionFactoryBuilderImpl()
        .withSqlQueriesLoggingEnabled(sqlLoggingProperties)
        .withSecondLevelCache(cachePropperties)
        .withDatabaseConnection(dbProperties);

    var sessionFactory = PersistenceContextProvider.createSessionFactory(sessionFactoryBuilder);
```

</details>

</details>

<br>

#### YAML configuration example

###### The YAML properties file should be placed in classpath 

<details>
<summary>Code snippet</summary>

```java
    var sessionFactory = PersistenceContextProvider.createSessionFactory(new YamlConfigurationSessionFactoryBuilderImpl()
  .withFilename("persistence-example.yaml"));
```

</details>

<details lang="yaml">
<summary>persistence-example.yaml</summary>

```yaml
database:
  url: jdbc:postgresql://rds-postgres-svydovets.c3bxmbbb5a4p.eu-central-1.rds.amazonaws.com:5432/svydovetsDB
  user: masterSvydovets
  password: demo4bibernate
  driverName: org.postgresql.Driver

connectionPool:
  maxPoolSize: 10
  minIdle: 5
  maxLifetime: 1800000
  connectionTimeout: 30000

secondLevelCache:
  enabled : true
  size : 200_000

sqlLogging:
  enabled : true
```
</details>
</details>

## TransactionManager
<details>
<summary>Description</summary>

Provides control over execution of transaction.  
If you need a transaction you can extract it from Session#getTransactionManager. Then, for starting transaction you 
need to <b>transactionManager.begin()</b>. Then can make some operations with DB and then commit or rollback changes.

<details>
<summary>Look at code snippet</summary>

```java
 TransactionManager transactionManager = session.getTransactionManager();
  try {
      transactionManager.begin();
      var person = session.findById(2, Person.class);

      person.setFirstName("new name");
    
      session.flush();
      
      transactionManager.commit();
  } catch (Exception ex) {
      transactionManager.rollback();
  }
```
</details>
</details>

## Session
<details>
<summary>Description</summary>

### Session is the main interface for working with ORM. 
The session is encapsulating connection to DB, and gives opportunities to perform CRUD operations.

<details>
<summary>How to open and close session:</summary>

```java
  SessionFactory sessionFactory = getSessionFactory();
  try (Session session = sessionFactory.openSession()) { //will be automatically closed
      //do smth
  } catch(Exception ex){
      // do smth
  }
```
```java
  SessionFactory sessionFactory = getSessionFactory();
  Session session;
  try {
      session = sessionFactory.openSession()
      //do smth
  } catch(Exception ex){
      // do smth
  } finally {
      if (!session.isClosed()) {
          sessiom.close();
        } 
  }
```
</details>

<details>
<summary>CRUD</summary>
Session provides methods for the CRUD operations:

<details>
<summary>findById</summary>
Searching in DB entity by its id.

```java
  SessionFactory sessionFactory = getSessionFactory();
  try (Session session = sessionFactory.openSession()) {
      var person = session.findById(2, Person.class);
      //do smth
  } catch(Exception ex){
      // do smth
  }
```
</details>
<details>
<summary>createTypedQuery</summary>

See more in the <a name="query">Query</a> block
</details>
<details>
<summary>persist</summary>
Insert entity into DB to proper table.

```java
  SessionFactory sessionFactory = getSessionFactory();
  try (Session session = sessionFactory.openSession()) {
      TransactionManager transactionManager = session.getTransactionManager();
      try {
      transactionManager.begin();
         
          session.persist(entity);
        
          transactionManager.commit();
      } catch (Exception ex) {
        transactionManager.rollback();
      }
  } catch(Exception ex){
      // do smth
  }
```
</details>
<details>
<summary>update</summary>

#TODO
</details>
<details>
<summary>remove</summary>

#TODO
</details>
</details>
</details>

## Cache
<details>
<summary>Description</summary>

Bibernate supports two levels of cache:
>1. Session cache. Is enabled by default and cannot be disabled. This cache is shared only within one session where its
> created.

>2. SessionFactory cache. Is disabled by default. Can be enabled via configuration (look at description above for 
>configuration). 
>Second level cache is working for entities that is marked as <b>@Cacheable</b>
>
><details>
><summary>Code snippet</summary>
>
>```java
>@Data
>@Entity
>@ToString
>@Table(name = "persons")
>@Cacheable
>public class Person {
>    @Id
>    private Long id;
>
>    @Column(name = "first_name")
>    private String firstName;
>    @Column(name = "last_name")
>    private String lastName;
>    private String email;
>    private int age;
>    @Column(name = "created_at")
>    private LocalDateTime createdAt;
>}
>```
></details>
><b>Also, pay attention that second level cache is custom realisation of Svydovets team, we don't use any external 
>tools for it.</b>

</details>

## Query
<details>
<summary>Description</summary>

Biberenate supports TypedQuery (and we are planing to provide NativeQuery in the future).
<b>TypedQuery</b> applies String SQL query and Class of entity and able to map result from DB but currently is unable
to map fields from entity to SQL query. For working with TypedQuery you need to provide SQL query in the same way as
for JDBC PreparedStatement.
Please take a look at code snippets bellow:
<details>
<summary>Snippets</summary>

<details>
<summary>SELECT without parameters:</summary>

```java
Query typedQuery = session.createTypedQuery("select * from persons", Person.class);
  var resultList = typedQuery.getResultList();
```
</details>
<details>
<summary>SELECT with parameter:</summary>

```java
Query typedQuery = session.createTypedQuery("select * from persons where first_name like ?", Person.class);
  typedQuery.addParameter("P%");
  Person firstResult = (Person) typedQuery.getFirstResult();
```
</details>
<details>
<summary>SELECT with parameters:</summary>

```java
Query typedQuery = session.createTypedQuery("select * from persons where first_name = ? and last_name = ?", Person.class);
  typedQuery.addParameter("P%");
  typedQuery.addParameter("P%");
  Person firstResult = (Person) typedQuery.getFirstResult();
```
</details>
<details>
<summary>INSERT:</summary>

```java
Query  query = session.createTypedQuery("insert into persons (first_name, last_name, email, age) values (?, ?, ?, ?);", Person.class);
  typedQuery.addParameter("FirstName");
  typedQuery.addParameter("LastName");
  typedQuery.addParameter("email");
  typedQuery.addParameter(1);
  query.execute();
```
</details>
<details>
<summary>UPDATE:</summary>

```java
Query  query = session.createTypedQuery("update persons set last_name = ? where id = ?;", Person.class);
  typedQuery.addParameter("LastName");
  typedQuery.addParameter(1);
  query.execute();
```
</details>
<details>
<summary>DELETE:</summary>

```java
Query  query = session.createTypedQuery("delete from persons where id = ?;", Person.class);
  typedQuery.addParameter(1);
  query.execute();
```
</details>
<details>
<summary>INSERT with TransactionalManager commit and rollback:</summary>

```java
 TransactionManager transactionManager = session.getTransactionManager();
  try {
      transactionManager.begin();
    
      Query  query = session.createTypedQuery("insert into persons (first_name, last_name, email, age) values (?, ?, ?, ?);", Person.class);
      typedQuery.addParameter("FirstName");
      typedQuery.addParameter("LastName");
      typedQuery.addParameter("email");
      typedQuery.addParameter(1);
      query.execute();
    
      transactionManager.commit();
  } catch (Exception ex) {
      transactionManager.rollback();
  }
```
</details>

</details>
</details>

## Mapping
<details>
<summary>Description</summary>
At this moment for selection bibernate supports only to-one associations (one-to-one and many-to-one)
this association must be specified only on the child entity side (entity with foreign key column)
and also must be marked with @JoinColumn annotation where specified foreign key column name.

```java
@Entity
@Table(name = "notes")
@Data
public class Note {

    @Id
    private Long id;

    @Column(name = "body")
    private String body;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
```
</details>

### Thanks for reading! Have fun!
