package com.dream_team.nsu_timetable_server.model;

import javax.persistence.*;

@Entity
public class GroupTimetable {

    @Id
    private int id;

    @Column(nullable = false)
    private TimetableRecord record;

    @OneToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public GroupTimetable() {
    }

    public GroupTimetable(TimetableRecord record, Group group) {
        this.record = record;
        this.group = group;
    }

    public TimetableRecord getRecord() {
        return record;
    }

    public void setRecord(TimetableRecord record) {
        this.record = record;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }
}
