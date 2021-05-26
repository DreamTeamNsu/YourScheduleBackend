package com.dream_team.nsu_timetable_server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:parser.properties")
@ConfigurationProperties(prefix = "parser")
@Getter
@Setter
public class ParserConfigs {
    private String specCoursesUrl;
    private String groupsListUrl;
    private String timetablesPrefixUrl;
    private int startCourse;
    private int endCourse;
}
