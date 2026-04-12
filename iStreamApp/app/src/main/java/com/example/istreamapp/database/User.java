package com.example.istreamapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String fullName;
    public String username;
    public String password;

    public User(String fullName, String username, String password) {
        this.fullName  = fullName;
        this.username  = username;
        this.password  = password;
    }
}