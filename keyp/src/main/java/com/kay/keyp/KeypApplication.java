package com.kay.keyp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kay.keyp"})
@EnableJpaRepositories(basePackages = "com.kay.keyp.repository")
@EntityScan(basePackages = "com.kay.keyp.entity")
public class KeypApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeypApplication.class, args);
	}

}
