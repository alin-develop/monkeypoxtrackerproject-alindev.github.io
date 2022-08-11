package com.alindevelop.monkeypoxtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonkeypoxTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonkeypoxTrackerApplication.class, args);
	}

}
