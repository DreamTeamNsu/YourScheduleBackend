package com.dream_team.nsu_timetable_server.model.repo;

import com.dream_team.nsu_timetable_server.model.GroupTimetable;
import com.dream_team.nsu_timetable_server.model.TimetableRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupTimetableRepo extends JpaRepository<GroupTimetable, Integer> {
    @Query("select timetable.record as timetableRecord from GroupTimetable timetable " +
            "where timetable.group.groupNumber = ?1")
    List<TimetableRecord> findTimetableRecordsByGroup(Integer groupNumber);
}
