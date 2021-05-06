package com.dream_team.nsu_timetable_server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Group {

    public Group() {
    }

    public Group(int groupNumber, int courseNumber) {
        this.groupNumber = groupNumber;
        this.courseNumber = courseNumber;
    }

    @Id
    private int groupNumber;

    // Todo calculate automatically
    @Column(nullable = false)
    private int courseNumber;

    public int getGroupNumber() {
        return groupNumber;
    }

    public int getCourseNumber() {
        return courseNumber;
    }
}
