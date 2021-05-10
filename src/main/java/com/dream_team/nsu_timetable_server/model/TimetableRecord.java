package com.dream_team.nsu_timetable_server.model;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class TimetableRecord {
    private TimetableCell cell;
    private Lesson lesson;

    public TimetableRecord() {
    }

    public TimetableRecord(TimetableCell cell, Lesson lesson) {
        this.cell = cell;
        this.lesson = lesson;
    }

    public TimetableCell getCell() {
        return cell;
    }

    public void setCell(TimetableCell cell) {
        this.cell = cell;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimetableRecord)) return false;
        TimetableRecord that = (TimetableRecord) o;
        return Objects.equals(cell, that.cell) && Objects.equals(lesson, that.lesson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell, lesson);
    }
}
