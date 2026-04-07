package com.example.personaleventplanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String category;
    private String location;
    private String date;
    private String time;
    private long dateTimestamp;

    public Event(String title, String category, String location, String date, String time, long dateTimestamp) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.date = date;
        this.time = time;
        this.dateTimestamp = dateTimestamp;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public long getDateTimestamp() { return dateTimestamp; }

    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setLocation(String location) { this.location = location; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setDateTimestamp(long dateTimestamp) { this.dateTimestamp = dateTimestamp; }
}