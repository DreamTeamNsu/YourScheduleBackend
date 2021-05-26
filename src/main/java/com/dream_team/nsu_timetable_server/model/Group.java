package com.dream_team.nsu_timetable_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups_table")
public class Group {
    @Id
    private int groupNumber;

    // Todo calculate automatically
    @Column(nullable = false)
    private int courseNumber;
}
