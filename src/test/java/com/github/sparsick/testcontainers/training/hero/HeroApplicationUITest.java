package com.github.sparsick.testcontainers.training.hero;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HeroApplicationUITest extends DatabaseSetup{

	@Container
	private static BrowserWebDriverContainer<?> SELENIUM = new BrowserWebDriverContainer<>()
			.withAccessToHost(true);

	@LocalServerPort
	private int localPort;

	private RemoteWebDriver browser;

	@BeforeEach
	void setUp() {
		Testcontainers.exposeHostPorts(localPort);
		browser = new RemoteWebDriver(SELENIUM.getSeleniumAddress(), new ChromeOptions());
		browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	}

	@AfterEach
	void cleanUp() {
		browser.quit();
	}

	@Test
	void titleIsHeroSearchMachine(){
		browser.get("http://host.testcontainers.internal:" + localPort + "/hero");
		WebElement title = browser.findElement(By.tagName("h1"));
		assertThat(title.getText().trim())
				.isEqualTo("Hero Search Machine");
	}

	@DynamicPropertySource
	static void databaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.username", () -> DATABASE.getUsername());
		registry.add("spring.datasource.password", () -> DATABASE.getPassword());
		registry.add("spring.datasource.url", () -> DATABASE.getJdbcUrl());

	}

}
