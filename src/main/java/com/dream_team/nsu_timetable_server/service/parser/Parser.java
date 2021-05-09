package com.dream_team.nsu_timetable_server.service.parser;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.parser.TimetablesParsingResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Parser {

    // Fields to cache some data
    private List<SpecCourse> cachedSpecCourses;
    private List<Group> cachedGroups;

    /**
     * Parse all groups numbers.
     * Source: https://table.nsu.ru/faculty/fit
     * @return List of all groups
     */
    public List<Group> parseGroupsNumber() {
        // Todo Write parsing logic
        // Todo Cache groups list into cachedGroups
        System.out.println("TEST");
        return List.of();
    }

    /**
     * Parse all SpecCourses.
     * Source:
     * @return List of spec courses
     */
    public List<SpecCourse> parseSpecCourses() {
        // Todo Write parsing logic
        // Todo Cache spec courses list into cachedSpecCourses
        return List.of();
    }

    /**
     * Parse timetable and create groups and spec courses table.
     * Use {@code cachedGroups} and {@code cachedSpecCourses}.
     * @return result contains groups timetable and spec courses timetable
     */
    public TimetablesParsingResult parseTimetables() {
        // Todo Write parsing logic
        return new TimetablesParsingResult();
    }

    public void clearCaches() {
        if(cachedGroups != null)
            cachedGroups.clear();
        if(cachedSpecCourses != null)
        cachedSpecCourses.clear();
    }
}
