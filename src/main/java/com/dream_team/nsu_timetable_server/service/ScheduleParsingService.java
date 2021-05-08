package com.dream_team.nsu_timetable_server.service;

import com.dream_team.nsu_timetable_server.service.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleParsingService {
    @Autowired
    private Parser parser;
    @Autowired
    private FillTimetableService fillTimetableService;
}
