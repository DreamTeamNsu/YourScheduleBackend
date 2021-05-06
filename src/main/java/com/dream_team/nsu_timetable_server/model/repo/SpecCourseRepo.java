package com.dream_team.nsu_timetable_server.model.repo;

import com.dream_team.nsu_timetable_server.model.SpecCourse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpecCourseRepo extends CrudRepository<SpecCourse, Integer> {

    // Todo group by to map?
    List<SpecCourse> findAllByCourseNumberOrderByBlockNumber(int courseNumber);
}
