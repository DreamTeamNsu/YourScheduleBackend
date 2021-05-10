package com.dream_team.nsu_timetable_server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "groups_table")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return groupNumber == group.groupNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupNumber);
    }
}
