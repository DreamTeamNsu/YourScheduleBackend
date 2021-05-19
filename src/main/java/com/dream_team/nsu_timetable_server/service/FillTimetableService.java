package com.dream_team.nsu_timetable_server.service;

import com.dream_team.nsu_timetable_server.model.*;
import com.dream_team.nsu_timetable_server.model.repo.GroupTimetableRepo;
import com.dream_team.nsu_timetable_server.model.repo.GroupsRepo;
import com.dream_team.nsu_timetable_server.model.repo.SpecCourseRepo;
import com.dream_team.nsu_timetable_server.model.repo.SpecTimetableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FillTimetableService {
    @Autowired
    private GroupsRepo groupsRepo;
    @Autowired
    private SpecCourseRepo specCourseRepo;
    @Autowired
    private GroupTimetableRepo groupTimetableRepo;
    @Autowired
    private SpecTimetableRepo specTimetableRepo;

    public void clearAll() {
        System.out.println("Start clearing...");
        groupTimetableRepo.deleteAll();
        specTimetableRepo.deleteAll();
        groupsRepo.deleteAll();
        specCourseRepo.deleteAll();
        System.out.println("Clearing completed.");
    }

    public void saveGroups(List<Group> groups) {
        if (groups != null) {
            groupsRepo.saveAll(groups);
            groupsRepo.flush();
        }
    }

    public void saveSpecCourses(List<SpecCourse> specCourses) {
        if (specCourses != null) {
            specCourseRepo.saveAll(specCourses);
            specCourseRepo.flush();
        }
    }

    public void saveGroupTimetable(Map<Group, List<TimetableRecord>> timetable) {
        if (timetable != null) {
            timetable.forEach(
                    (key, value) -> groupTimetableRepo.saveAll(value
                            .stream()
                            .map(currentValue -> new GroupTimetable(currentValue, key))
                            .collect(Collectors.toList()))
            );
            groupTimetableRepo.flush();
        }
    }

    public void saveSpecCoursesTimetable(Map<SpecCourse, Set<TimetableRecord>> timetable) {
        if (timetable != null) {
            timetable.forEach(
                    (key, value) -> specTimetableRepo.saveAll(value
                            .stream()
                            .map(currentValue -> new SpecCourseTimetable(currentValue, key))
                            .collect(Collectors.toList()))
            );
            specTimetableRepo.flush();
        }
    }
}