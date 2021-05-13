package com.dream_team.nsu_timetable_server.service;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.SpecCourseTimetable;
import com.dream_team.nsu_timetable_server.model.TimetableRecord;
import com.dream_team.nsu_timetable_server.model.repo.GroupTimetableRepo;
import com.dream_team.nsu_timetable_server.model.repo.GroupsRepo;
import com.dream_team.nsu_timetable_server.model.repo.SpecCourseRepo;
import com.dream_team.nsu_timetable_server.model.repo.SpecTimetableRepo;
import com.dream_team.nsu_timetable_server.model.response.GroupTimetableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    @Transactional
    public Iterable<Group> getGroups() {
        return groupsRepo.findAll();
    }

    @Transactional
    public GroupTimetableResponse getGroupTimetable(int groupNumber) {
        var opt = groupsRepo.findById(groupNumber);
        if (opt.isPresent()) {
            var group = opt.get();
            var timetable = groupTimetableRepo.findTimetableRecordsByGroup(group.getGroupNumber());
            var spec = specRepo
                    .findAllByCourseNumberOrderByBlockNumber(group.getCourseNumber())
                    .stream()
                    .collect(Collectors.groupingBy(SpecCourse::getBlockNumber));
            return new GroupTimetableResponse(timetable, spec);
        } else throw new IllegalArgumentException("Wrong group number");
    }

    @Transactional
    public List<TimetableRecord> getOnlyGroupTimetable(int groupNumber) {
        var opt = groupsRepo.findById(groupNumber);
        if (opt.isPresent()) {
            var group = opt.get();
            return groupTimetableRepo.findTimetableRecordsByGroup(group.getGroupNumber());
        } else throw new IllegalArgumentException("Wrong group number");
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    public List<TimetableRecord> getSpecTimetable(List<Integer> specCourses) {
        List<TimetableRecord> timetable = new ArrayList<>();
        specCourses.forEach(specId -> {
            var opt = specRepo.findById(specId);
            if (opt.isPresent()) {
                var spec = opt.get();
                timetable.addAll(
                        specTimetableRepo
                        .findAllBySpecCourse(spec)
                        .stream()
                        .map(SpecCourseTimetable::getRecord)
                        .collect(Collectors.toList())
                );
            } else throw new IllegalArgumentException("Wrong spec id");
        });
        return timetable;
    }
}