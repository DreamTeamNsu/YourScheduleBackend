package com.dream_team.nsu_timetable_server.service;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.SpecCourseTimetable;
import com.dream_team.nsu_timetable_server.model.TimetableRecord;
import com.dream_team.nsu_timetable_server.model.repo.GroupTimetableRepo;
import com.dream_team.nsu_timetable_server.model.repo.GroupsRepo;
import com.dream_team.nsu_timetable_server.model.repo.SpecCourseRepo;
import com.dream_team.nsu_timetable_server.model.repo.SpecTimetableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimetableRequestsService {

    @Autowired
    private GroupsRepo groupsRepo;
    @Autowired
    private SpecCourseRepo specRepo;
    @Autowired
    private GroupTimetableRepo groupTimetableRepo;
    @Autowired
    private SpecTimetableRepo specTimetableRepo;

    public Iterable<Group> getGroups() {
        return groupsRepo.findAll();
    }

    public List<TimetableRecord> getGroupTimetable(int groupNumber) {
        var opt = groupsRepo.findById(groupNumber);
        if (opt.isPresent()) {
            var group = opt.get();
            return groupTimetableRepo.findTimetableRecordsByGroup(group.getGroupNumber());
        } else throw new IllegalArgumentException("Wrong group number");
    }

    public Map<Integer, List<SpecCourse>> getSpecCourses(int groupNumber) {
        var opt = groupsRepo.findById(groupNumber);
        if (opt.isPresent()) {
            var group = opt.get();
            return specRepo
                    .findAllByCourseNumberOrderByBlockNumber(group.getCourseNumber())
                    .stream()
                    .collect(Collectors.groupingBy(SpecCourse::getBlockNumber));
        } else throw new IllegalArgumentException("Wrong group number");
    }

    public List<TimetableRecord> getSpecTimetable(int specId) {
        var opt = specRepo.findById(specId);
        if (opt.isPresent()) {
            var spec = opt.get();
            return specTimetableRepo
                    .findAllBySpecCourse(spec)
                    .stream()
                    .map(SpecCourseTimetable::getRecord)
                    .collect(Collectors.toList());
        } else throw new IllegalArgumentException("Wrong spec id");
    }

}
