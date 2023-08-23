package com.github.sparsick.testcontainers.training.hero.universum;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class HeroRepositoryTest {

    private HeroRepository repositoryUnderTest;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:tc:postgresql:9.6.8:///hero?TC_INITSCRIPT=db/changelog/initDB.sql");
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