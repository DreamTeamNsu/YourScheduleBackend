package com.dream_team.nsu_timetable_server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class SpecCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int blockNumber;

    @Column(nullable = false)
    private int courseNumber;

    public SpecCourse(@NonNull String name, int blockNumber, int courseNumber) {
        this.name = name;
        this.blockNumber = blockNumber;
        this.courseNumber = courseNumber;
    }
}