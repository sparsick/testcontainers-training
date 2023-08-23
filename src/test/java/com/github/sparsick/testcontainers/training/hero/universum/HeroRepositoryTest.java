package com.github.sparsick.testcontainers.training.hero.universum;

import com.github.sparsick.testcontainers.training.hero.DatabaseSetup;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class HeroRepositoryTest extends DatabaseSetup {


    private HeroRepository repositoryUnderTest;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        var dataSource = new HikariDataSource();
        dataSource.setUsername(DATABASE.getUsername());
        dataSource.setPassword(DATABASE.getPassword());
        dataSource.setJdbcUrl(DATABASE.getJdbcUrl());
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