package com.example.sportsfeedapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class Bookmark {
    @PrimaryKey
    public int id;
    public String title;
    public String description;
    public int imageRes;
    public String category;

    public Bookmark(int id, String title, String description, int imageRes, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageRes = imageRes;
        this.category = category;
    }
}