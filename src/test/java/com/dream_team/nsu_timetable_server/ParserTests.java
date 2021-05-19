package com.dream_team.nsu_timetable_server;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.parser.TimetablesParsingResult;
import com.dream_team.nsu_timetable_server.service.parser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTests {

    private static Parser parser;
    private static String groupsDocPath;
    private static String specCoursesDocPath;
    private static String timetablesDirectoryPath;

    private static TimetablesParsingResult timetable;
    private static List<Group> groups;
    private static List<SpecCourse> specCourses;

    @BeforeAll
    public static void beforeAll() throws IOException {
        parser = new Parser();

        // Init paths
        Properties properties = new Properties();
        properties.load(ParserTests.class.getResourceAsStream("/parser-test.properties"));
        groupsDocPath = properties.getProperty("groups_doc_path");
        specCoursesDocPath = properties.getProperty("spec_courses_doc_path");
        timetablesDirectoryPath = properties.getProperty("timetables_directory_path");

        // Parse groups numbers
        InputStream groupsDoc = ParserTests.class.getResourceAsStream(groupsDocPath);
        groups = parser.parseGroupsNumber(Jsoup.parse(groupsDoc, "UTF-8", ""));

        // Parse spec courses
        InputStream specCoursesDoc = ParserTests.class.getResourceAsStream(specCoursesDocPath);
        specCourses = parser.parseSpecCourses(Jsoup.parse(specCoursesDoc, "UTF-8", ""), 3, 4);

        // Parse timetables
        Map<Group, Document> groupsDocumentsMap = groups
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        group -> {
                            InputStream groupDoc = ParserTests.class
                                    .getResourceAsStream(timetablesDirectoryPath + group.getGroupNumber() + ".html");
                            try {
                                return Jsoup.parse(groupDoc, "UTF-8", "");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }));
        timetable = parser.parseTimetables(specCourses, groupsDocumentsMap, 3);
    }

    @Test
    public void testGroupsList() {
        assertEquals(41, groups.size());
        assertTrue(groups.contains(new Group(18210, 3)));
        assertTrue(groups.contains(new Group(19210, 2)));
        assertTrue(groups.contains(new Group(20215, 1)));
        assertFalse(groups.stream().anyMatch(group -> group.getCourseNumber() == 4));
    }

    @Test
    public void testSpecCourses() {
        assertEquals(39, specCourses.size());
        assertEquals(1, specCourses
                .stream()
                .filter(specCourse -> specCourse.getName().equals("Теория и практика нейронных сетей -- КафКТ"))
                .count());
        assertEquals(6, specCourses
                .stream()
                .filter(specCourse -> specCourse.getBlockNumber() == 1)
                .count());
    }

    @Test
    public void testGroupTimetable() {
        assertNotNull(timetable.getGroupsTimetable());
        assertFalse(timetable.getGroupsTimetable().isEmpty());

        assertTrue(timetable.getGroupsTimetable().keySet().containsAll(groups));

        assertNotEquals(
                timetable.getGroupsTimetable().get(new Group(18210, 3)),
                timetable.getGroupsTimetable().get(new Group(18209, 3)));

        assertEquals("Базы данных", timetable.getGroupsTimetable()
                .get(new Group(18210, 3))
                .stream().filter(record ->
                        record.getCell().getDayOfWeek().equals(DayOfWeek.FRIDAY)
                                && record.getCell().getOrderNumber() == 6)
                .findFirst()
                .get()
                .getLesson()
                .getName()
        );
    }

    @Test
    public void testSpecTimetable() {
        assertNotNull(timetable.getSpecCoursesTimetable());
        assertFalse(timetable.getSpecCoursesTimetable().isEmpty());

        var deepLearning = specCourses.stream()
                .filter(specCourse -> specCourse
                        .getName()
                        .startsWith("Современные методы глубокого машинного обучения"))
                .findFirst().get();

        assertEquals(3, timetable.getSpecCoursesTimetable()
                .get(deepLearning)
                .stream()
                .filter(record -> record.getCell().getDayOfWeek().equals(DayOfWeek.FRIDAY))
                .findFirst()
                .get()
                .getCell().getOrderNumber());

        assertEquals(2, (int) timetable.getSpecCoursesTimetable()
                .get(deepLearning)
                .stream()
                .filter(record -> record.getCell().getDayOfWeek().equals(DayOfWeek.TUESDAY))
                .count());
    }
}