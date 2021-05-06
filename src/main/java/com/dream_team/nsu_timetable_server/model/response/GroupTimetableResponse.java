package com.dream_team.nsu_timetable_server.model.response;

import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.TimetableRecord;

import java.util.List;
import java.util.Map;

public class GroupTimetableResponse {

    public GroupTimetableResponse() {
    }

    public GroupTimetableResponse(List<TimetableRecord> timetable, Map<Integer, List<SpecCourse>> specCourses) {
        this.timetable = timetable;
        this.specCourses = specCourses;
    }

    private List<TimetableRecord> timetable;
    // By blocks
    private Map<Integer, List<SpecCourse>> specCourses;

    public List<TimetableRecord> getTimetable() {
        return timetable;
    }

    public void setTimetable(List<TimetableRecord> timetable) {
        this.timetable = timetable;
    }

    public Map<Integer, List<SpecCourse>> getSpecCourses() {
        return specCourses;
    }

    public void setSpecCourses(Map<Integer, List<SpecCourse>> specCourses) {
        this.specCourses = specCourses;
    }
}
