package com.example.sportsfeedapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bookmark bookmark);

    @Delete
    void delete(Bookmark bookmark);

    @Query("SELECT * FROM bookmarks")
    LiveData<List<Bookmark>> getAllBookmarks();

    @Query("SELECT id FROM bookmarks")
    List<Integer> getAllIds();
}