package com.dream_team.nsu_timetable_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TimetableCell {

    @Column(nullable = false)
    private int orderNumber;

    @NonNull
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @NonNull
    @Column(nullable = false)
    private LocalTime startTime;

    @NonNull
    @Column(nullable = false)
    private LocalTime endTime;

    @NonNull
    @Column(nullable = false)
    private Week week;

}