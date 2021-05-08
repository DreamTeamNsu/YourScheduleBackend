package com.dream_team.nsu_timetable_server.model.repo;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.GroupTimetable;
import com.dream_team.nsu_timetable_server.model.TimetableRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupTimetableRepo extends CrudRepository<GroupTimetable, Integer> {

    List<GroupTimetable> findAllByGroup(Group group);

    @Query("select timetable.record as timetableRecord from GroupTimetable timetable " +
            "where timetable.group.groupNumber = ?1")
    List<TimetableRecord> findTimetableRecordsByGroup(Integer groupNumber);
}
