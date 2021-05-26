package com.dream_team.nsu_timetable_server.service.parser;

import com.dream_team.nsu_timetable_server.config.ParserConfigs;
import com.dream_team.nsu_timetable_server.exception.ParsingException;
import com.dream_team.nsu_timetable_server.model.Group;
import com.dream_team.nsu_timetable_server.model.SpecCourse;
import com.dream_team.nsu_timetable_server.model.parser.TimetablesParsingResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ParsingRequestsManagementService {

    private final Parser parser;
    private final ParserConfigs parserConfigs;

    private final String specPath;
    private final String groupsPath;
    private final String groupTimetablePrefixPath;

    private List<Group> groupsCache;
    private List<SpecCourse> specCoursesCache;

    @Autowired
    public ParsingRequestsManagementService(Parser parser, ParserConfigs parserConfigs) {
        this.parser = parser;
        this.parserConfigs = parserConfigs;
        this.groupsPath = parserConfigs.getGroupsListUrl();
        this.specPath = parserConfigs.getSpecCoursesUrl();
        this.groupTimetablePrefixPath = parserConfigs.getTimetablesPrefixUrl();
    }

    public List<Group> parseGroups() throws IOException {
        groupsCache = parser.parseGroupsNumber(Jsoup.connect(groupsPath).get());
        return groupsCache;
    }

    public List<SpecCourse> parseSpecCourses() throws IOException {
        specCoursesCache = parser.parseSpecCourses(
                Jsoup.connect(specPath).get(),
                parserConfigs.getStartCourse(),
                parserConfigs.getEndCourse()
        );
        return specCoursesCache;
    }

    public TimetablesParsingResult parseTimetables() {
        Map<Group, Document> groupsDocumentsMap = groupsCache
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        group -> {
                            try {
                                return Jsoup.connect(groupTimetablePrefixPath + group.getGroupNumber()).get();
                            } catch (IOException e) {
                                throw new ParsingException(
                                        "Can not load document from "
                                                + groupTimetablePrefixPath
                                                + group.getGroupNumber(),
                                        e);
                            }
                        }));
            return parser.parseTimetables(specCoursesCache, groupsDocumentsMap, parserConfigs.getStartCourse());
    }
}