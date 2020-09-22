package io.dolittle.lunch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"io.dolittle.lunch.config"})
@Slf4j
public class LunchApplication {

	public static void main(String[] args) {
		SpringApplication.run(LunchApplication.class, args);
		log.info("APP STARTED: Dolittle Lunch");
	}

}
