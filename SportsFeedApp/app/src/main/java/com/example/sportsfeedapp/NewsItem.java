package com.example.sportsfeedapp;

public class NewsItem {
    private int id;
    private String title;
    private String description;
    private int imageRes;
    private String category;
    private boolean featured;

    public NewsItem(int id, String title, String description, int imageRes, String category, boolean featured) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageRes = imageRes;
        this.category = category;
        this.featured = featured;
    }

    public int getId()           { return id; }
    public String getTitle()     { return title; }
    public String getDescription() { return description; }
    public int getImageRes()     { return imageRes; }
    public String getCategory()  { return category; }
    public boolean isFeatured()  { return featured; }
}