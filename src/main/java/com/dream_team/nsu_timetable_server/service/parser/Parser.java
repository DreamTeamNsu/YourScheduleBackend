package com.dream_team.nsu_timetable_server.service.parser;

import com.dream_team.nsu_timetable_server.model.*;
import com.dream_team.nsu_timetable_server.model.parser.TimetablesParsingResult;
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

    /**
     * Parse all groups numbers.
     * @return List of all groups
     */
    public List<Group> parseGroupsNumber(Document page) {
        List<Group> groups = new ArrayList<>();
        try {

            Element groupsTable = getGroupsListTable(page);
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
                    groups.add(new Group(Integer.parseInt(group.text()), course));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return groups;
    }

    /**
     * Parse all SpecCourses.
     * @return List of spec courses
     */
    public List<SpecCourse> parseSpecCourses(Document page, int startCourse, int stopCourse) {
        List<SpecCourse> specCourses =  new ArrayList<>();
        try {

            int blockNum = 0;
            for (int course = startCourse; course <= stopCourse; course++) {
                Element specCoursesTable_crs = getSpecCoursesTable(course, page, startCourse);
                Elements tRows_crs = specCoursesTable_crs.select("tr");

                Element name;
                for (Element row : tRows_crs) {
                    //Block numbers
                    if (row.select("th").size() != 0) {
                        blockNum++;
                    }
                    //Spec names
                    else if ((name = row.selectFirst("strong")) != null) {
                        specCourses.add(new SpecCourse(name.text().substring(3), blockNum, course));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return specCourses;
    }

    /**
     * Parse timetable and create groups and spec courses table.
     * @return result contains groups timetable and spec courses timetable
     */
    public TimetablesParsingResult parseTimetables(List<SpecCourse> specCourses,
                                                   Map<Group, Document> groupDocumentMap,
                                                   int startCourse) {

        Map<Group, List<TimetableRecord>> groupsTimetable = new HashMap<>();
        Map<SpecCourse, Set<TimetableRecord>> specCoursesTimetable = new HashMap<>();

        //Initialize Timetable
        for (SpecCourse spc: specCourses) {
            specCoursesTimetable.put(spc, new HashSet<>());
        }

        try{

            for (Group grp: groupDocumentMap.keySet()) {
                groupsTimetable.put(grp, new ArrayList<>());
                //System.out.println(group);
                Element groupTimeTable = getGroupTimeTable(groupDocumentMap.get(grp));

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

                        if (grp.getCourseNumber() >= startCourse) {
                            for (Map.Entry<SpecCourse, Set<TimetableRecord>> spc : specCoursesTimetable.entrySet()) {
                                if (spc.getKey().getName().contains(name + " --")) {
                                    spc.getValue().add(record);
                                    isInSpecCourses = true;
                                    break;
                                }
                            }
                        }

                        if (!isInSpecCourses) {
                            groupsTimetable.get(grp).add(record);
                        }
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

            if (roomAndBuilding[1].startsWith("Ауд."))
                roomAndBuilding[1] = roomAndBuilding[1].substring(5);

            return roomAndBuilding;
        }
    }

    private LessonType parseLessonTypeFromElement(Element lessonTypeElement) {
        return switch (lessonTypeElement.attr("title")) {
            case "практическое занятие" -> LessonType.SEMINAR;
            case "лекция" -> LessonType.LECTURE;
            case "лабораторное занятие" -> LessonType.LAB;
            default -> LessonType.UNKNOWN;
        };
    }

    private Week parseWeekFromElement(Element weekElement) {
        if (weekElement == null) {
            return Week.ALL;
        } else if (weekElement.text().contains("Нечетная")) {
            return Week.ODD;
        } else return Week.EVEN;
    }

    // Todo we can add choice bw bachelor and mag
    private Element getSpecCoursesTable(int course, Document page, int startCourse) throws IOException {
        int course_element = course - startCourse;
        Element specCoursesContainer = page.select("div[class=\"col-lg-9 col-md-8 col-sm-7 content-bar\"]").first();
        Element bacCourseElement = specCoursesContainer.select("div[class=\"program-card-section-wrap\"]").get(course_element);
        Element bacCourseCurrYearElement = bacCourseElement.selectFirst("div[class=\"green-panel staff_question\"]");
        return bacCourseCurrYearElement.selectFirst("tbody");
    }

    // Todo we can add choice bw bachelor and mag
    private Element getGroupsListTable(Document page) throws IOException {
        Element bachelorContainer = page.select("table[class=\"degree_groups\"]").get(0);
        return bachelorContainer.selectFirst("tbody");
    }

    // Todo we can add choice bw bachelor and mag
    private Element getGroupTimeTable(Document page) throws IOException {
        Element timeTable = page.selectFirst("table[class=\"time-table\"]");
        return timeTable.selectFirst("tbody");
    }
}