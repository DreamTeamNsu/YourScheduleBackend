package com.dream_team.nsu_timetable_server.service.parser;

import com.dream_team.nsu_timetable_server.model.*;
import com.dream_team.nsu_timetable_server.model.parser.TimetablesParsingResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Component
public class Parser {

    // Fields to cache some data
    private List<SpecCourse> cachedSpecCourses;
    private List<Group> cachedGroups;

    /**
     * Parse all groups numbers.
     * Source: https://table.nsu.ru/faculty/fit
     *
     * @return List of all groups
     */
    public List<Group> parseGroupsNumber() {
        // Todo Write parsing logic
        // Todo Cache groups list into cachedGroups
        try {
            if (cachedGroups == null) {
                cachedGroups = new ArrayList<>();
            }

            Element groupsTable = getGroupsListTable();
            Elements tRows = groupsTable.select("td");

            int course = 0;
            Element group;
            for (Element row : tRows) {
                //Course numbers
                if (row.select("h4").size() != 0) {
                    course++;
                }
                //Group numbers
                else if ((group = row.selectFirst("a[class=\"group\"]")) != null) {
                    cachedGroups.add(new Group(Integer.parseInt(group.text()), course));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cachedGroups;
    }

    /**
     * Parse all SpecCourses.
     * Source: https://www.nsu.ru/n/information-technologies-department/students/raspred.php
     *
     * @return List of spec courses
     */
    public List<SpecCourse> parseSpecCourses() {
        // Todo Write parsing logic
        // Todo Cache spec courses list into cachedSpecCourses
        try {
            if (cachedSpecCourses == null) {
                cachedSpecCourses = new ArrayList<>();
            }

            int blockNum = 0;
            for (int course = 3; course <= 4; course++) {
                Element specCoursesTable_crs = getSpecCoursesTable(course);
                Elements tRows_crs = specCoursesTable_crs.select("tr");

                Element name;
                for (Element row : tRows_crs) {
                    //Block numbers
                    if (row.select("th").size() != 0) {
                        blockNum++;
                    }
                    //Spec names
                    else if ((name = row.selectFirst("strong")) != null) {
                        cachedSpecCourses.add(new SpecCourse(name.text().substring(3), blockNum, course));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cachedSpecCourses;
    }

    /**
     * Parse timetable and create groups and spec courses table.
     * Use {@code cachedGroups} and {@code cachedSpecCourses}.
     *
     * @return result contains groups timetable and spec courses timetable
     */
    public TimetablesParsingResult parseTimetables() {
        // Todo Write parsing logic
        if (cachedGroups == null) {
            parseGroupsNumber();
        }
        if (cachedSpecCourses == null) {
            parseSpecCourses();
        }

        Map<Group, List<TimetableRecord>> groupsTimetable = new HashMap<>();
        Map<SpecCourse, Set<TimetableRecord>> specCoursesTimetable = new HashMap<>();



        //Initialize Timetables
        for (SpecCourse spc: cachedSpecCourses) {
            specCoursesTimetable.put(spc, new HashSet<>());
        }

        for (Group grp: cachedGroups) {
            groupsTimetable.put(grp, new ArrayList<>());

        }

        try{

            for (Group grp: cachedGroups) {
                groupsTimetable.put(grp, new ArrayList<>());
                //System.out.println(group);
                Element groupTimeTable = getGroupTimeTable(grp.getGroupNumber());

                Elements ttRows = groupTimeTable.select("tr");

                int paraNum = 0;

                for (Element paraRow : ttRows) {

                    if (paraNum == 0) {
                        paraNum++;
                        continue;
                    }

                    Elements ttParaColumns = paraRow.select("td");

                    String[] textTime = ttParaColumns.get(0).text().split(":");
                    LocalTime startTime = LocalTime.of(Integer.parseInt(textTime[0]), Integer.parseInt(textTime[1]));
                    LocalTime endTime = startTime.plusHours(1).plusMinutes(35);

                    int dayNum = 1;

                    for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                        //Skip SUNDAY
                        if (dayOfWeek == DayOfWeek.SUNDAY) break;

                        Element currentCellContent = ttParaColumns.get(dayNum).selectFirst("div[class=\"cell\"]");

                        //Skip empty cells
                        if (currentCellContent == null) {
                            //System.out.println("null");
                            dayNum++;
                            continue;
                        }

                        //Get TimetableCell
                        Element weekElement = currentCellContent.selectFirst("div[class=\"week\"]");
                        Week week = parseWeekFromElement(weekElement);
                        TimetableCell timetableCell = new TimetableCell(paraNum, dayOfWeek, startTime, endTime, week);

                        //Get Lesson
                        Element lessonTypeElement = currentCellContent.getElementsByAttributeValueContaining("class", "type").first();
                        LessonType lessonType = parseLessonTypeFromElement(lessonTypeElement);
                        String name = currentCellContent.selectFirst("div[class=\"subject\"]").attr("title");
                        Element teacherElement = currentCellContent.selectFirst("a[class=\"tutor\"]");
                        String teacher = teacherElement == null ? null : teacherElement.text();
                        Element roomAndBuildingElement = currentCellContent.getElementsByAttributeValue("class", "\"room\"").first();
                        String[] roomAndBuilding = parseRoomAndBuildingFromElement(roomAndBuildingElement);
                        Lesson lesson = new Lesson(lessonType, name, teacher, roomAndBuilding[0], roomAndBuilding[1]);

                        TimetableRecord record = new TimetableRecord(timetableCell, lesson);

                        boolean isInSpecCourses = false;

                        //System.out.print(grp.getGroupNumber()+" ");

                        if (grp.getCourseNumber() >=3) {
                            for (Map.Entry<SpecCourse, Set<TimetableRecord>> spc : specCoursesTimetable.entrySet()) {
                                if (spc.getKey().getName().contains(name + " --")) {
                                    spc.getValue().add(record);
                                    //System.out.println("spec " + spc.getKey().getName());
                                    isInSpecCourses = true;
                                    break;
                                }
                            }
                        }

                        if (!isInSpecCourses) {
                            groupsTimetable.get(grp).add(record);
                            //System.out.println("group");
                        }

                    /*System.out.println(timetableCell.getOrderNumber() +" "
                            + timetableCell.getDayOfWeek() +" "
                            + timetableCell.getStartTime() +" "
                            + timetableCell.getEndTime() +" "
                            + timetableCell.getWeek());

                    System.out.println(lesson.getType() +" "
                            +lesson.getName()+" "
                            +lesson.getTeacher()+" "
                            +lesson.getRoom()+" "
                            +lesson.getBuilding());*/

                        dayNum++;
                    }

                    paraNum++;
                }
            }

        } catch (IOException e) {
            System.err.println("Timetables Parsing Result Failed");
            e.printStackTrace();
        }


        return new TimetablesParsingResult(groupsTimetable, specCoursesTimetable);
    }

    public void clearCaches() {
        if (cachedGroups != null)
            cachedGroups.clear();
        if (cachedSpecCourses != null)
            cachedSpecCourses.clear();
    }


    private String[] parseRoomAndBuildingFromElement(Element roomAndBuildingElement) {
        String[] roomAndBuilding = new String[]{null, null};
        if (roomAndBuildingElement == null) return roomAndBuilding;
        else {
            String text = roomAndBuildingElement.text();
            if (text.endsWith("ГК")) {
                roomAndBuilding[0] = text.substring(0, text.length() - 3);
                roomAndBuilding[1] = "Главный корпус";
            } else if (text.endsWith("ЛК")) {
                roomAndBuilding[0] = text.substring(0, text.length() - 3);
                roomAndBuilding[1] = "Лабораторный корпус";
            } else if (text.endsWith("Спортивный комплекс")) {
                roomAndBuilding[1] = "Спортивный комплекс";
            } else if (text.length() <= 5 && text.matches(".*\\d+.*")) {
                roomAndBuilding[0] = text;
                roomAndBuilding[1] = "Новый корпус";
            } else {
                roomAndBuilding[1] = text;

            }

            if (roomAndBuilding[0] != null && roomAndBuilding[0].startsWith("Ауд."))
                roomAndBuilding[0] = roomAndBuilding[0].substring(5);

            if (roomAndBuilding[1] != null && roomAndBuilding[1].startsWith("Ауд."))
                roomAndBuilding[1] = roomAndBuilding[1].substring(5);

            return roomAndBuilding;
        }
    }

    private LessonType parseLessonTypeFromElement(Element lessonTypeElement) {
        LessonType lessonType = switch (lessonTypeElement.attr("title")) {
            case "практическое занятие" -> LessonType.SEMINAR;
            case "лекция" -> LessonType.LECTURE;
            case "лабораторное занятие" -> LessonType.LAB;
            default -> LessonType.UNKNOWN;
        };
        //System.out.println(lessonTypeElement.attr("title"));
        return lessonType;
    }

    private Week parseWeekFromElement(Element weekElement) {
        if (weekElement == null) {
            return Week.ALL;
        } else if (weekElement.text().contains("Нечетная")) {
            return Week.ODD;
        } else return Week.EVEN;
    }

    private Element getSpecCoursesTable(int course) throws IOException { //todo we can add choice bw bachelor and maga
        int beginCourse = 3;
        int course_element = course - beginCourse;
        Document page = Jsoup.connect("https://www.nsu.ru/n/information-technologies-department/students/raspred.php").get();
        Element specCoursesContainer = page.select("div[class=\"col-lg-9 col-md-8 col-sm-7 content-bar\"]").first();
        Element bacCourse_i = specCoursesContainer.select("div[class=\"program-card-section-wrap\"]").get(course_element);
        Element bacCourse_i_currYear = bacCourse_i.selectFirst("div[class=\"green-panel staff_question\"]");
        return bacCourse_i_currYear.selectFirst("tbody");
    }

    private Element getGroupsListTable() throws IOException { //todo we can add choice bw bachelor, maga and aspirantura

        Document page = Jsoup.connect("https://table.nsu.ru/faculty/fit").get();
        Element bachelorContainer = page.select("table[class=\"degree_groups\"]").get(0);
        return bachelorContainer.selectFirst("tbody");
    }

    private Element getGroupTimeTable(int group) throws IOException { //todo we can add choice bw bachelor, maga and aspirantura
        Document page = Jsoup.connect("https://table.nsu.ru/group/" + group).get();
        Element timeTable = page.selectFirst("table[class=\"time-table\"]");
        return timeTable.selectFirst("tbody");
    }
}
