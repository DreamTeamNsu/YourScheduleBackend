package com.dream_team.nsu_timetable_server;

import com.dream_team.nsu_timetable_server.service.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ParserTest {

    private static Parser parser;

    @BeforeAll
    public static void beforeAll() {
        parser = new Parser();
    }

    @Test
    public void testParser() {
        parser.parseTimetables();
        /*System.out.println("SIZE: " + size);
        Assertions.assertEquals(0, size);*/
    }
}