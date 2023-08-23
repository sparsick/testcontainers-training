# Testcontainers Training

![Build Status](https://github.com/sparsick/socrates23-testcontainers-training/workflows/MavenBuild/badge.svg)

Spring Boot Demo Application

```
mvn clean install
mvn spring-boot:run
```

## Helpful links

### Testcontainers JDBC Support

- https://java.testcontainers.org/quickstart/junit_5_quickstart/
- https://java.testcontainers.org/modules/databases/jdbc/
- https://java.testcontainers.org/modules/databases/jdbc/#using-a-classpath-init-script
- https://java.testcontainers.org/modules/databases/postgres/


### Testcontainers via @Testcontainers and @Container

- https://java.testcontainers.org/features/image_name_substitution/
- https://hub.docker.com/_/postgres
- https://blog.sandra-parsick.de/2020/05/21/using-testcontainers-in-spring-boot-tests-for-database-integration-tests/
- https://spring.io/blog/2023/06/23/improved-testcontainers-support-in-spring-boot-3-1
- https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.testcontainers

### Testcontainers and the singelton pattern

- https://java.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers
- https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.data-migration.spring.liquibase.enabled