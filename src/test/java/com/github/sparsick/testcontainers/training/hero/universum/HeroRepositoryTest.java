package com.github.sparsick.testcontainers.training.hero.universum;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class HeroRepositoryTest {

    @Container
    private PostgreSQLContainer database = new PostgreSQLContainer(DockerImageName.parse("postgres:15.4"));

    private HeroRepository repositoryUnderTest;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        ScriptUtils.runInitScript(new JdbcDatabaseDelegate(database, ""), "db/changelog/initDB.sql" );

        var dataSource = new HikariDataSource();
        dataSource.setUsername(database.getUsername());
        dataSource.setPassword(database.getPassword());
        dataSource.setJdbcUrl(database.getJdbcUrl());
        jdbcTemplate = new JdbcTemplate(dataSource);

        repositoryUnderTest = new HeroRepository(dataSource);
    }

    @AfterEach
    void teardown() {
        jdbcTemplate.update("DELETE FROM hero");
    }

    @Test
    void addHeros(){
        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));

        var result = jdbcTemplate.queryForObject("Select count(*) from hero", Integer.class);

        assertThat(result).isEqualTo(1);
    }

    @Test
    void allHeros(){
        jdbcTemplate.update("insert into hero (city, name, universum) values ('Metropolis','Superman','MARVEL')");
        Collection<Hero> allHeros = repositoryUnderTest.allHeros();
        assertThat(allHeros).hasSize(1);
    }

}