package com.dream_team.nsu_timetable_server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Embeddable
public class TimetableRecord {

    @NonNull
    private TimetableCell cell;

    @NonNull
    private Lesson lesson;

}
