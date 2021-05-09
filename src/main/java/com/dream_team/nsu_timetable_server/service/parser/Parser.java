package com.dream_team.nsu_timetable_server.service.parser;

import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.parser.TimetablesParsingResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Parser {

    // Fields to cache some data
    private List<SpecCourse> cachedSpecCourses;
    private List<Group> cachedGroups;

    /**
     * Parse all groups numbers.
     * Source: https://table.nsu.ru/faculty/fit
     * @return List of all groups
     */
    public List<Group> parseGroupsNumber() {
        // Todo Write parsing logic
        // Todo Cache groups list into cachedGroups
        System.out.println("TEST");
        return List.of();
    }

    /**
     * Parse all SpecCourses.
     * Source: https://www.nsu.ru/n/information-technologies-department/students/raspred.php
     * @return List of spec courses
     */
    public List<SpecCourse> parseSpecCourses() {
        // Todo Write parsing logic
        // Todo Cache spec courses list into cachedSpecCourses
        try{
            if (cachedSpecCourses == null){
                cachedSpecCourses = new ArrayList<>();
            }

            int blockNum = 0;
            for (int course = 3; course <= 4; course++) {
                Element specCoursesTable_crs = getSpecCoursesTable(course);
                Elements tRows_crs = specCoursesTable_crs.select("tr");

                Element name;
                for (Element row: tRows_crs){
                    //Block numbers
                    if (row.select("th").size() != 0) {
                        blockNum++;
                    }
                    //Spec names
                    else if ((name = row.selectFirst("strong")) != null){
                        cachedSpecCourses.add(new SpecCourse(name.text().substring(2), blockNum, course));
                    }
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        return cachedSpecCourses;
    }

    /**
     * Parse timetable and create groups and spec courses table.
     * Use {@code cachedGroups} and {@code cachedSpecCourses}.
     * @return result contains groups timetable and spec courses timetable
     */
    public TimetablesParsingResult parseTimetables() {
        // Todo Write parsing logic
        return new TimetablesParsingResult();
    }

    public void clearCaches() {
        cachedGroups.clear();
        cachedSpecCourses.clear();
    }

    public void testParsing() {
        /*try{
            if (cachedSpecCourses == null){
                cachedSpecCourses = new ArrayList<SpecCourse>();
            }

            int blockNum = 0;
            for (int course = 3; course <= 4; course++) {
                Element specCoursesTable_crs = getSpecСoursesTable(course);
                Elements tRows_crs = specCoursesTable_crs.select("tr");

                Element name;
                for (Element row: tRows_crs){
                    //Block numbers
                    if (row.select("th").size() != 0) {
                        blockNum++;
                    }
                    //Spec names
                    else if ((name = row.selectFirst("strong")) != null){
                        String strName = name.text();
                        cachedSpecCourses.add(new SpecCourse(strName.substring(2), blockNum, course));
                    }
                }
            }

            for (SpecCourse spc: cachedSpecCourses) {
                System.out.println(spc.getId() +" "+ spc.getName()+" "+ spc.getBlockNumber()+" "+ spc.getCourseNumber());
            }

        }catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    private Element getSpecCoursesTable(int course) throws IOException{ //todo we can add choice bw bacalavriat and maga
        int beginCourse = 3;
        int course_element = course - beginCourse;
        Document page = Jsoup.connect("https://www.nsu.ru/n/information-technologies-department/students/raspred.php").get();
        Element specCoursesContainer = page.select("div[class=\"col-lg-9 col-md-8 col-sm-7 content-bar\"]").first();
        Element bacCourse_i = specCoursesContainer.select("div[class=\"program-card-section-wrap\"]").get(course_element);
        Element bacCourse_i_currYear = bacCourse_i.selectFirst("div[class=\"green-panel staff_question\"]");
        return bacCourse_i_currYear.selectFirst("tbody");
    }
}
