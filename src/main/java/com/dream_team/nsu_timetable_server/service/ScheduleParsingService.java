package com.dream_team.nsu_timetable_server.service;

import com.dream_team.nsu_timetable_server.service.parser.ParsingRequestsManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
public class ScheduleParsingService {
    private final ParsingRequestsManagementService parsingRequestsManagementService;
    private final FillTimetableService fillTimetableService;

    public ScheduleParsingService(
            @Autowired ParsingRequestsManagementService parsingRequestsManagementService,
            @Autowired FillTimetableService fillTimetableService) {
        this.parsingRequestsManagementService = parsingRequestsManagementService;
        this.fillTimetableService = fillTimetableService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 4 * * 0-6")
    @Transactional
    public void fillData() throws IOException {
        System.out.println("Start scheduled task...");
        fillTimetableService.clearAll();
        var groups = parsingRequestsManagementService.parseGroups();
        fillTimetableService.saveGroups(groups);
        var specCourses = parsingRequestsManagementService.parseSpecCourses();
        fillTimetableService.saveSpecCourses(specCourses);
        var res = parsingRequestsManagementService.parseTimetables();
        fillTimetableService.saveGroupTimetable(res.getGroupsTimetable());
        fillTimetableService.saveSpecCoursesTimetable(res.getSpecCoursesTimetable());
        System.out.println("End scheduled task.");
    }
}
