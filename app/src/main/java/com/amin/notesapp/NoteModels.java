package com.amin.notesapp;

public class NoteModels {
    private int id;
    private String title;
    private String desc;
    private String ClockTime;
    private String Date;
    private String CDate;
    private int remember;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClockTime() {
        return ClockTime;
    }

    public void setClockTime(String clockTime) {
        ClockTime = clockTime;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCDate() {
        return CDate;
    }

    public void setCDate(String CDate) {
        this.CDate = CDate;
    }

    public int getRemember() {
        return remember;
    }

    public void setRemember(int remember) {
        this.remember = remember;
    }
}
