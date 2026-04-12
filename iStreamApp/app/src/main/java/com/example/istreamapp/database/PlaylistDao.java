package com.example.istreamapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert
    void addToPlaylist(PlaylistItem item);

    @Query("SELECT * FROM playlist WHERE userId = :userId ORDER BY id DESC")
    LiveData<List<PlaylistItem>> getPlaylistForUser(int userId);

    @Delete
    void remove(PlaylistItem item);

    @Query("SELECT * FROM playlist WHERE userId = :userId AND videoUrl = :url LIMIT 1")
    PlaylistItem findDuplicate(int userId, String url);
}