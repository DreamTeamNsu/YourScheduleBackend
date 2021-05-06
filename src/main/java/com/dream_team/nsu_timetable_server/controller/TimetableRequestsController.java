package com.dream_team.nsu_timetable_server.controller;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.TimetableRecord;
import com.dream_team.nsu_timetable_server.model.response.GroupTimetableResponse;
import com.dream_team.nsu_timetable_server.service.TimetableRequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TimetableRequestsController {

    @Autowired
    private TimetableRequestsService service;

    // Todo test
    @GetMapping("/get/groups")
    public @ResponseBody
    Iterable<Group> getGroups() {
        return service.getGroups();
    }

    // Todo
    @GetMapping("/get/group-timetable")
    public @ResponseBody
    GroupTimetableResponse getGroupTimetableWithSpec(@RequestParam int groupNumber) {
        return service.getGroupTimetableAndSpecList(groupNumber);
    }

    // Todo check if we need spec info
    // Todo
    @GetMapping("/get/spec-timetable")
    public @ResponseBody
    List<TimetableRecord> getSpecTimetable(@RequestParam int specId) {
        return service.getSpecTimetable(specId);
    }
}