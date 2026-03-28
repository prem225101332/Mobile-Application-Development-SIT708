package com.example.quizzapp;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {

    private static final String PREFS_NAME = "quiz_prefs";
    private static final String KEY_DARK_MODE = "is_dark_mode";
    private static final String KEY_PLAYER_NAME = "player_name";

    public static boolean isDarkMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public static void toggleTheme(Context context) {
        boolean newDarkMode = !isDarkMode(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_DARK_MODE, newDarkMode).apply();
        applyTheme(newDarkMode);
    }

    public static void applySavedTheme(Context context) {
        boolean darkMode = isDarkMode(context);
        applyTheme(darkMode);
    }
    private static void applyTheme(boolean darkMode) {
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    public static void savePlayerName(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_PLAYER_NAME, name).apply();
    }
    public static String getSavedPlayerName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PLAYER_NAME, "");
    }
}