package com.dream_team.nsu_timetable_server.model;

import com.dream_team.nsu_timetable_server.model.LessonType;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Lesson {

    private LessonType type;
    private String name;
    private String teacher;
    private String room;
    private String building;

    public Lesson(LessonType type, String name, String teacher, String room, String building) {
        this.type = type;
        this.name = name;
        this.teacher = teacher;
        this.room = room;
        this.building = building;
    }

    public Lesson() {
    }

    public LessonType getType() {
        return type;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}