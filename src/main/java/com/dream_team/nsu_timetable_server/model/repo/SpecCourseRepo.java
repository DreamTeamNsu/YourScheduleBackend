package com.dream_team.nsu_timetable_server.model.repo;

import com.dream_team.nsu_timetable_server.model.SpecCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpecCourseRepo extends JpaRepository<SpecCourse, Integer> {
    List<SpecCourse> findAllByCourseNumberOrderByBlockNumber(int courseNumber);
}
