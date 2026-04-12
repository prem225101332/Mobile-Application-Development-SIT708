package com.example.istreamapp.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("istream_session", Context.MODE_PRIVATE);
    }

    public void saveUser(int userId, String username, String fullName) {
        prefs.edit()
                .putInt("user_id", userId)
                .putString("username", username)
                .putString("full_name", fullName)
                .apply();
    }

    public int getUserId()       { return prefs.getInt("user_id", -1); }
    public String getUsername()  { return prefs.getString("username", ""); }
    public String getFullName()  { return prefs.getString("full_name", ""); }
    public boolean isLoggedIn()  { return getUserId() != -1; }

    public void logout() {
        prefs.edit().clear().apply();
    }
}