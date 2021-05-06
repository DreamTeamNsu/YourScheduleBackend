package com.dream_team.nsu_timetable_server.model.repo;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.GroupTimetable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupTimetableRepo extends CrudRepository<GroupTimetable, Integer> {

    List<GroupTimetable> findAllByGroup(Group group);
}
