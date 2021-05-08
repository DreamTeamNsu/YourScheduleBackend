package com.dream_team.nsu_timetable_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NsuTimetableServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NsuTimetableServerApplication.class, args);
	}

}
