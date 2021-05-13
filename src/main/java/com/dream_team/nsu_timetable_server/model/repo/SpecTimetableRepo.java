package com.dream_team.nsu_timetable_server.model.repo;

import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.SpecCourseTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecTimetableRepo extends JpaRepository<SpecCourseTimetable, Integer> {
    List<SpecCourseTimetable> findAllBySpecCourse(SpecCourse spec);
}
