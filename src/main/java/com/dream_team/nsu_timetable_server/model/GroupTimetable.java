package com.dream_team.nsu_timetable_server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class GroupTimetable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    @NonNull
    private TimetableRecord record;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "group_id")
    @NonNull
    private Group group;

}
