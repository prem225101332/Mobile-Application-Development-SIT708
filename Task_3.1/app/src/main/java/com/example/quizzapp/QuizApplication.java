package com.example.quizzapp;

import android.app.Application;

public class QuizApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeHelper.applySavedTheme(this);
    }
}