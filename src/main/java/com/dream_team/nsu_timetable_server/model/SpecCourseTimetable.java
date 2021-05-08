package com.dream_team.nsu_timetable_server.model;

import javax.persistence.*;

@Entity
public class SpecCourseTimetable {
    @Id
    private int id;

    @Column(nullable = false)
    private TimetableRecord record;

    @OneToOne
    @JoinColumn(name = "spec_id")
    private SpecCourse specCourse;

    public SpecCourseTimetable() {
    }

    public SpecCourseTimetable(TimetableRecord record, SpecCourse specCourse) {
        this.record = record;
        this.specCourse = specCourse;
    }

    public TimetableRecord getRecord() {
        return record;
    }

    public void setRecord(TimetableRecord record) {
        this.record = record;
    }

    public SpecCourse getSpecCourse() {
        return specCourse;
    }

    public void setSpecCourse(SpecCourse specCourse) {
        this.specCourse = specCourse;
    }

    public int getId() {
        return id;
    }
}
