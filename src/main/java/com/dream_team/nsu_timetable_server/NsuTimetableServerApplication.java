package com.dream_team.nsu_timetable_server;

import com.dream_team.nsu_timetable_server.config.ParserConfigs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties(ParserConfigs.class)
@SpringBootApplication
public class NsuTimetableServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NsuTimetableServerApplication.class, args);
    }
}
