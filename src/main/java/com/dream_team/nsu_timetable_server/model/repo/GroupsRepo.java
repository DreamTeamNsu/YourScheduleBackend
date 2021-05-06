package com.dream_team.nsu_timetable_server.model.repo;

import com.dream_team.nsu_timetable_server.model.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupsRepo extends CrudRepository<Group, Integer> {

}