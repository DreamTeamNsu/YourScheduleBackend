package com.dream_team.nsu_timetable_server.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
public class TimetableCell {

    public TimetableCell() {
    }

    public TimetableCell(int orderNumber, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Week week) {
        this.orderNumber = orderNumber;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.week = week;
    }

    @Column(nullable = false)
    private int orderNumber;

    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private Week week;

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimetableCell)) return false;
        TimetableCell that = (TimetableCell) o;
        return orderNumber == that.orderNumber
                && dayOfWeek == that.dayOfWeek
                && startTime.equals(that.startTime)
                && endTime.equals(that.endTime)
                && week == that.week;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, dayOfWeek, startTime, endTime, week);
    }
}