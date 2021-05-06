package com.dream_team.nsu_timetable_server.service;

import com.dream_team.nsu_timetable_server.model.*;
import com.dream_team.nsu_timetable_server.model.repo.GroupTimetableRepo;
import com.dream_team.nsu_timetable_server.model.repo.GroupsRepo;
import com.dream_team.nsu_timetable_server.model.repo.SpecCourseRepo;
import com.dream_team.nsu_timetable_server.model.repo.SpecTimetableRepo;
import com.dream_team.nsu_timetable_server.model.response.GroupTimetableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    // Todo refactor
    public GroupTimetableResponse getGroupTimetableAndSpecList(int groupNumber) {
        var opt = groupsRepo.findById(groupNumber);
        if(opt.isPresent()) {
            var group = opt.get();
            // Todo is it ok?
            var timetable = groupTimetableRepo
                    .findAllByGroup(group)
                    .stream()
                    .map(GroupTimetable::getRecord)
                    .collect(Collectors.toList());

            // Todo test
            var specList = specRepo
                    .findAllByCourseNumberOrderByBlockNumber(group.getCourseNumber())
                    .stream()
                    .collect(Collectors.groupingBy(SpecCourse::getBlockNumber));

            return new GroupTimetableResponse(timetable, specList);

        } else throw new IllegalArgumentException("Wrong group number");
    }

    public List<TimetableRecord> getSpecTimetable(int specId) {
        var opt = specRepo.findById(specId);
        if(opt.isPresent()) {
            var spec = opt.get();
            return specTimetableRepo
                    .findAllBySpecCourse(spec)
                    .stream()
                    .map(SpecCourseTimetable::getRecord)
                    .collect(Collectors.toList());
        } else throw new IllegalArgumentException("Wrong spec id");
    }

}
