package com.dream_team.nsu_timetable_server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class SpecCourseTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @NonNull
    @Column(nullable = false)
    private TimetableRecord record;

    @NonNull
    @OneToOne
    @JoinColumn(name = "spec_id")
    private SpecCourse specCourse;

    public SpecCourseTimetable(@NonNull TimetableRecord record, @NonNull SpecCourse specCourse) {
        this.record = record;
        this.specCourse = specCourse;
    }
}
