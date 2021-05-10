package com.dream_team.nsu_timetable_server.controller;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.TimetableRecord;
import com.dream_team.nsu_timetable_server.model.response.GroupTimetableResponse;
import com.dream_team.nsu_timetable_server.service.TimetableRequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TimetableRequestsController {

    @Autowired
    private TimetableRequestsService service;

    @GetMapping("/get/groups")
    public @ResponseBody
    Iterable<Group> getGroups() {
        return service.getGroups();
    }

    @GetMapping("/get/group-timetable")
    public @ResponseBody
    GroupTimetableResponse getGroupTimetable(@RequestParam int groupNumber) {
        return service.getGroupTimetable(groupNumber);
    }

    @GetMapping("/get/only-group-timetable")
    public @ResponseBody
    List<TimetableRecord> getOnlyGroupTimetable(@RequestParam int groupNumber) {
        return service.getOnlyGroupTimetable(groupNumber);
    }

    @GetMapping("/get/spec-courses")
    public @ResponseBody
    Map<Integer, List<SpecCourse>> getSpecCourses(@RequestParam int groupNumber) {
        return service.getSpecCourses(groupNumber);
    }

    // Todo check if we need spec info
    @GetMapping("/get/spec-timetable")
    public @ResponseBody
    List<TimetableRecord> getSpecTimetable(@RequestParam int specId) {
        return service.getSpecTimetable(specId);
    }

    @GetMapping("/get/spec-array-timetable")
    public @ResponseBody List<TimetableRecord> getSpecArrayTimetable(@RequestParam String[] id) {
        return service.getSpecTimetable(Arrays.stream(id).map(Integer::parseInt).collect(Collectors.toList()));
    }
}