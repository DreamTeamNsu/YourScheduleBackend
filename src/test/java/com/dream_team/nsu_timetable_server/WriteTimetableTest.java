package com.dream_team.nsu_timetable_server;

import com.dream_team.nsu_timetable_server.model.*;
import com.dream_team.nsu_timetable_server.service.FillTimetableService;
import com.dream_team.nsu_timetable_server.service.TimetableRequestsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(value = "SpringBootTest.WebEnvironment.MOCK", classes = NsuTimetableServerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integration-test.properties")
@ActiveProfiles("test")
public class WriteTimetableTest {

    @Autowired FillTimetableService fillTimetableService;
    @Autowired TimetableRequestsService timetableRequestsService;

    @Test
    public void testGroupsFillingAndReading() {
        var g10 = new Group(18210, 3);
        var g9 = new Group(18209, 3);
        var g3 = new Group(19203, 2);
        var g1 = new Group(20201, 1);
        fillTimetableService.saveGroups(List.of(g1, g3, g9, g10));
        var groups = timetableRequestsService.getGroups();
        assertEquals(4, groups.size());
        assertTrue(groups.containsAll(List.of(g1, g3, g9, g10)));
    }

    @Test
    public void testSpecCoursesFillingAndReading() {
        var g10 = new Group(18210, 3);
        var spec1 = new SpecCourse("TestSpec1", 1, 3);
        var spec2 = new SpecCourse("TestSpec2", 2, 3);
        var spec3 = new SpecCourse("TestSpec3", 1, 3);
        fillTimetableService.saveGroups(List.of(g10));
        fillTimetableService.saveSpecCourses(List.of(spec1, spec2, spec3));
        var specCourses = timetableRequestsService.getSpecCourses(g10.getGroupNumber());
        assertTrue(specCourses.containsKey(1));
        assertTrue(specCourses.containsKey(2));
        assertTrue(specCourses.get(1).containsAll(List.of(spec3, spec1)));
        assertTrue(specCourses.get(2).contains(spec2));
    }

    @Test
    public void testGroupTimetableFillingAndReading() {
        var g10 = new Group(18210, 3);
        var g9 = new Group(18209, 3);
        fillTimetableService.saveGroups(List.of(g10, g9));
        Map<Group, List<TimetableRecord>> timetable = new HashMap<>();
        List<TimetableRecord> tt10 = new ArrayList<>();
        List<TimetableRecord> tt9 = new ArrayList<>();
        // Fill timetable lists

        var lecture = new TimetableRecord(
                new TimetableCell(
                        1,
                        DayOfWeek.MONDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 35),
                        Week.ALL),
                new Lesson(
                        LessonType.LECTURE,
                        "TestLecture",
                        "TestTeacher1",
                        "12000",
                        "Building"));
        var seminar = new TimetableRecord(
                new TimetableCell(
                        2,
                        DayOfWeek.MONDAY,
                        LocalTime.of(10, 50),
                        LocalTime.of(12, 25),
                        Week.ALL),
                new Lesson(
                        LessonType.SEMINAR,
                        "TestSeminar",
                        "TestTeacher2",
                        "12020",
                        "Building2"));

        tt10.add(lecture);
        tt10.add(seminar);
        tt9.add(lecture);
        timetable.put(g10, tt10);
        timetable.put(g9, tt9);

        fillTimetableService.saveGroupTimetable(timetable);
        var realTimetable10 = timetableRequestsService.getOnlyGroupTimetable(g10.getGroupNumber());
        assertEquals(2, realTimetable10.size());
        assertTrue(realTimetable10.containsAll(List.of(lecture, seminar)));
        var realTimetable9 = timetableRequestsService.getOnlyGroupTimetable(g9.getGroupNumber());
        assertEquals(1, realTimetable9.size());
        assertTrue(realTimetable9.contains(lecture));
    }
}
