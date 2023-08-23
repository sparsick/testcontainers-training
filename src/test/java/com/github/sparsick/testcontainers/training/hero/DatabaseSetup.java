package com.github.sparsick.testcontainers.training.hero;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class DatabaseSetup {

    @Container
    public static final PostgreSQLContainer DATABASE = new PostgreSQLContainer(DockerImageName.parse("postgres:15.4"));

    @BeforeAll
    static void databaseSetup() {
        ScriptUtils.runInitScript(new JdbcDatabaseDelegate(DATABASE, ""), "db/changelog/initDB.sql" );
    }
}
