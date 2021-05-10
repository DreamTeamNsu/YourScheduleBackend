package com.dream_team.nsu_timetable_server;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.parser.TimetablesParsingResult;
import com.dream_team.nsu_timetable_server.service.parser.Parser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private static Parser parser;

    @BeforeAll
    public static void beforeAll() {
        parser = new Parser();
    }

    @Test
    public void testParser() {

        TimetablesParsingResult timetable = new TimetablesParsingResult();
        List<Group> groups = new ArrayList<>();
        List<SpecCourse> specCourses = new ArrayList<>();

        assertTimeout(Duration.of(1, ChronoUnit.MINUTES),
                () -> {
                    groups.addAll(parser.parseGroupsNumber());
                    specCourses.addAll(parser.parseSpecCourses());
                    var tt = parser.parseTimetables();
                    timetable.setGroupsTimetable(tt.getGroupsTimetable());
                    timetable.setSpecCoursesTimetable(tt.getSpecCoursesTimetable());
                }

        );

        var mapper = new ObjectMapper();
        assertNotNull(timetable.getGroupsTimetable());
        assertTrue(timetable.getGroupsTimetable().size() > 0);

//        assertDoesNotThrow(
//                () -> System.out.println(mapper.writeValueAsString(timetable.getGroupsTimetable()))
//        );

        assertTrue(timetable.getGroupsTimetable().keySet().containsAll(groups));

//        System.out.println("\n");
//        assertDoesNotThrow(
//                () -> System.out.println(mapper.writeValueAsString(timetable.getGroupsTimetable()
//                        .get(new Group(18210, 3))))
//        );
//        System.out.println("\n");

        assertEquals(
                timetable.getGroupsTimetable().get(new Group(18210, 3)),
                        timetable.getGroupsTimetable().get(new Group(18209, 3)));
        System.out.println("EQUALS");

        assertEquals("Базы данных", timetable.getGroupsTimetable()
                .get(new Group(18210, 3))
                .stream().filter(record ->
                        record.getCell().getDayOfWeek().equals(DayOfWeek.FRIDAY)
                                /*&& record.getCell().getOrderNumber() == 6*/)
                .findFirst()
                .get()
                .getLesson()
                .getName()
        );

        assertNotNull(timetable.getSpecCoursesTimetable());
        assertTrue(timetable.getSpecCoursesTimetable().size() > 0);
        assertDoesNotThrow(
                () -> System.out.println(mapper.writeValueAsString(timetable.getSpecCoursesTimetable()))
        );

        var smgmo = parser.parseSpecCourses().stream()
                .filter(specCourse -> specCourse
                        .getName()
                        .startsWith("Современные методы глубокого машинного обучения"))
                .findFirst().get();

        assertEquals(3, timetable.getSpecCoursesTimetable()
                .get(smgmo)
                .stream()
                .filter(record -> record.getCell().getDayOfWeek().equals(DayOfWeek.FRIDAY))
                .findFirst()
                .get()
                .getCell().getOrderNumber());
    }
}