package com.github.sparsick.testcontainers.training.hero;


import com.github.sparsick.testcontainers.training.hero.universum.ComicUniversum;
import com.github.sparsick.testcontainers.training.hero.universum.Hero;
import com.github.sparsick.testcontainers.training.hero.universum.HeroController;
import com.github.sparsick.testcontainers.training.hero.universum.NewHeroModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.sql.DataSource;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HeroApplicationTests {

	@Autowired
	private DataSource dataSource;


	@Autowired
	private HeroController controller;
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void setUp() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@AfterEach
	void cleanUp() {
		jdbcTemplate.update("DELETE FROM hero");
	}

	@Test
	void addNewHero() {
		var model = new NewHeroModel();
		model.setHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
		controller.addNewHero(model);

		var result = jdbcTemplate.queryForObject("Select count(*) from hero", Integer.class);

		assertThat(result).isEqualTo(1);
	}

	@Test
	void viewHeros() {
		jdbcTemplate.update("insert into hero (city, name, universum) values ('Metropolis','Superman','MARVEL')");

		Model model = new ExtendedModelMap();
		controller.viewHeros("", model);

		Collection<Hero> heros = (Collection<Hero>) model.getAttribute("heros");

		assertThat(heros).hasSize(1);
	}

}
