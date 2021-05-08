package com.dream_team.nsu_timetable_server.model.parser;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.TimetableRecord;

import java.util.List;
import java.util.Map;

public class TimetablesParsingResult {

    private Map<Group, List<TimetableRecord>> groupsTimetable;
    private Map<SpecCourse, List<TimetableRecord>> specCoursesTimetable;

    public TimetablesParsingResult() {
    }

    public TimetablesParsingResult(Map<Group, List<TimetableRecord>> groupsTimetable,
                                   Map<SpecCourse, List<TimetableRecord>> specCoursesTimetable) {
        this.groupsTimetable = groupsTimetable;
        this.specCoursesTimetable = specCoursesTimetable;
    }

    public Map<Group, List<TimetableRecord>> getGroupsTimetable() {
        return groupsTimetable;
    }

    public void setGroupsTimetable(Map<Group, List<TimetableRecord>> groupsTimetable) {
        this.groupsTimetable = groupsTimetable;
    }

    public Map<SpecCourse, List<TimetableRecord>> getSpecCoursesTimetable() {
        return specCoursesTimetable;
    }

    public void setSpecCoursesTimetable(Map<SpecCourse, List<TimetableRecord>> specCoursesTimetable) {
        this.specCoursesTimetable = specCoursesTimetable;
    }
}
