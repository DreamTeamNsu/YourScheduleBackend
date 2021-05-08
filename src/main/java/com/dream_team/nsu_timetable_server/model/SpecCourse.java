package com.dream_team.nsu_timetable_server.model;

import javax.persistence.*;

@Entity
public class SpecCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    // TODO Just int numbers?
    private int blockNumber;

    @Column(nullable = false)
    private int courseNumber;

    // Use this constructor in com.dream_team.nsu_timetable_server.service.parser
    public SpecCourse(String name, int blockNumber, int courseNumber) {
        this.name = name;
        this.blockNumber = blockNumber;
        this.courseNumber = courseNumber;
    }

    public SpecCourse() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }
}