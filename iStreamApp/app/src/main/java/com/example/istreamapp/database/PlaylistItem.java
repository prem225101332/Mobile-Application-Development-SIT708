package com.example.istreamapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist")
public class PlaylistItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String videoUrl;
    public String videoTitle;

    public PlaylistItem(int userId, String videoUrl, String videoTitle) {
        this.userId     = userId;
        this.videoUrl   = videoUrl;
        this.videoTitle = videoTitle;
    }
}