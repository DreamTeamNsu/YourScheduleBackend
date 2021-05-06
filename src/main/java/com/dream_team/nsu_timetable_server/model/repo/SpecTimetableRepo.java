package com.dream_team.nsu_timetable_server.model.repo;

import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.SpecCourseTimetable;
import com.dream_team.nsu_timetable_server.model.TimetableRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpecTimetableRepo extends CrudRepository<SpecCourseTimetable, Integer> {
    List<SpecCourseTimetable> findAllBySpecCourse(SpecCourse spec);
}
