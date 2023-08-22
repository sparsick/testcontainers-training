package com.github.sparsick.testcontainers.training.hero.universum;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HeroRepositoryTest {

    private HeroRepository repositoryUnderTest;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() throws SQLException, LiquibaseException {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("password");
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
        Liquibase liquibase = new liquibase.Liquibase("db/changelog/initDB.sql", new ClassLoaderResourceAccessor(), database);
        liquibase.update();

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