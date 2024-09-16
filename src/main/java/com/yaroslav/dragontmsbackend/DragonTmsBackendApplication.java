package com.yaroslav.dragontmsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DragonTmsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DragonTmsBackendApplication.class, args);
	}

}
