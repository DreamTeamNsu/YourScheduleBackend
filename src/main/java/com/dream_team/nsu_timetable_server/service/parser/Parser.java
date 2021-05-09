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
        try{
            if (cachedGroups == null){
                cachedGroups = new ArrayList<>();
            }

            Element groupsTable = getGroupsTable();
            Elements tRows = groupsTable.select("td");

            int course = 0;
            Element group;
            for (Element row: tRows) {
                //Course numbers
                if (row.select("h4").size() != 0) {
                    course++;
                }
                //Group numbers
                else if ((group = row.selectFirst("a[class=\"group\"]")) != null){
                    cachedGroups.add(new Group(Integer.parseInt(group.text()), course));
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        return cachedGroups;
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
        if(cachedGroups != null)
            cachedGroups.clear();
        if(cachedSpecCourses != null)
        cachedSpecCourses.clear();
    }

    public void testParsing() {
        /*try{
            if (cachedGroups == null){
                cachedGroups = new ArrayList<>();
            }

            Element groupsTable = getGroupsTable();
            Elements tRows = groupsTable.select("td");

            int course = 0;
            Element group;
            for (Element row: tRows) {
                //Course numbers
                if (row.select("h4").size() != 0) {
                    course++;
                }
                //Group numbers
                else if ((group = row.selectFirst("a[class=\"group\"]")) != null){
                    cachedGroups.add(new Group(Integer.parseInt(group.text()), course));
                }
            }

            for (Group grp: cachedGroups) {
                System.out.println(grp.getGroupNumber()+" "+ grp.getCourseNumber());
            }

        }catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    private Element getSpecCoursesTable(int course) throws IOException{ //todo we can add choice bw bachelor and maga
        int beginCourse = 3;
        int course_element = course - beginCourse;
        Document page = Jsoup.connect("https://www.nsu.ru/n/information-technologies-department/students/raspred.php").get();
        Element specCoursesContainer = page.select("div[class=\"col-lg-9 col-md-8 col-sm-7 content-bar\"]").first();
        Element bacCourse_i = specCoursesContainer.select("div[class=\"program-card-section-wrap\"]").get(course_element);
        Element bacCourse_i_currYear = bacCourse_i.selectFirst("div[class=\"green-panel staff_question\"]");
        return bacCourse_i_currYear.selectFirst("tbody");
    }

    private Element getGroupsTable() throws IOException{ //todo we can add choice bw bachelor, maga and aspirantura

        Document page = Jsoup.connect("https://table.nsu.ru/faculty/fit").get();
        Element bachelorContainer = page.select("table[class=\"degree_groups\"]").get(0);
        return bachelorContainer.selectFirst("tbody");
    }
}
