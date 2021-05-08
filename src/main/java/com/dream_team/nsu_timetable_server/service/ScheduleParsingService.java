package com.dream_team.nsu_timetable_server.service;

import com.dream_team.nsu_timetable_server.service.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleParsingService {
    @Autowired
    private Parser parser;
    @Autowired
    private FillTimetableService fillTimetableService;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 4 * * 0-6")
    public void fillData() {
        // Todo use normal logging
        System.out.println("Start scheduled task...");
        fillTimetableService.saveGroups(parser.parseGroupsNumber());
        fillTimetableService.saveSpecCourses(parser.parseSpecCourses());
        var res = parser.parseTimetables();
        fillTimetableService.saveGroupTimetable(res.getGroupsTimetable());
        fillTimetableService.saveSpecCoursesTimetable(res.getSpecCoursesTimetable());
        parser.clearCaches();
        System.out.println("End scheduled task.");
    }

}
