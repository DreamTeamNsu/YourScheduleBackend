package com.dream_team.nsu_timetable_server.model.repo;

import com.dream_team.nsu_timetable_server.model.GroupTimetable;
import com.dream_team.nsu_timetable_server.model.TimetableRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupTimetableRepo extends JpaRepository<GroupTimetable, Integer> {
    @Query("""
            select timetable.record as timetableRecord from GroupTimetable timetable
                where timetable.group.groupNumber = :group_number
            """)
    List<TimetableRecord> findTimetableRecordsByGroup(@Param("group_number") Integer groupNumber);
}
