package com.example.chatbot;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class Message {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String text;
    public String sender;
    public long timestamp;
    public String username;
    public Message(String text, String sender, long timestamp, String username) {
        this.text = text;
        this.sender = sender;
        this.timestamp = timestamp;
        this.username = username;
    }
}