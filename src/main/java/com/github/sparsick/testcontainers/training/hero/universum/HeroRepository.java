package com.github.sparsick.testcontainers.training.hero.universum;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;

@Repository
public class HeroRepository {

    private final JdbcTemplate jdbcTemplate;

    public HeroRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addHero(Hero hero) {
        jdbcTemplate.update("insert into hero (city, name, universum) values (?,?,?)",
                hero.getCity(), hero.getName(), hero.getUniversum().name());

    }

    public Collection<Hero> allHeros() {
        return jdbcTemplate.query("select * From hero",
                (resultSet, i) -> new Hero(resultSet.getString("name"),
                        resultSet.getString("city"),
                        ComicUniversum.valueOf(resultSet.getString("universum"))));
    }

    public Collection<Hero> findHerosBySearchCriteria(String searchCriteria) {
        return null;
    }
}
