package com.dream_team.nsu_timetable_server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@Embeddable
public class Lesson {

    @Column(nullable = false)
    @NonNull
    private LessonType type;

    @Column(nullable = false)
    @NonNull
    private String name;

    private String teacher;
    private String room;
    private String building;

    public Lesson(@NonNull LessonType type, @NonNull String name, String teacher, String room, String building) {
        this.type = type;
        this.name = name;
        this.teacher = teacher;
        this.room = room;
        this.building = building;
    }
}