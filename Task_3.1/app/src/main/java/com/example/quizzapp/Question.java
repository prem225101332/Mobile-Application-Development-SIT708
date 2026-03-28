package com.example.quizzapp;

import java.util.List;

public class Question {

    private String text;
    private List<String> options;
    private int correctIndex;
    private String emoji;

    public Question(String text, List<String> options, int correctIndex, String emoji) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.emoji = emoji;
    }
    public String getText() {
        return text;
    }
    public List<String> getOptions() {
        return options;
    }
    public int getCorrectIndex() {
        return correctIndex;
    }
    public String getEmoji() {
        return emoji;
    }
}