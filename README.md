# Bibernate ORM

### ORM framework made by Svydovets team.

## Configuration

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


### Thanks for reading! Have fun!
