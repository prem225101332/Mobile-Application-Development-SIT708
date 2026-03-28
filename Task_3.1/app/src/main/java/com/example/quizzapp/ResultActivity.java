package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class ResultActivity extends AppCompatActivity {
    private TextView playerNameText;
    private TextView scoreText;
    private TextView performanceMessage;
    private MaterialButton newQuizButton;
    private MaterialButton finishButton;
    private Button themeToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String playerName = getIntent().getStringExtra("player_name");
        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 10);

        playerNameText = findViewById(R.id.tvPlayerName);
        scoreText = findViewById(R.id.tvScore);
        newQuizButton = findViewById(R.id.btnNewQuiz);
        finishButton = findViewById(R.id.btnFinish);
        themeToggleButton = findViewById(R.id.btnThemeToggle);

        playerNameText.setText(playerName + "!");
        scoreText.setText(score + " / " + total);

        updateThemeToggleIcon();
        newQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewQuiz();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishApp();
            }
        });

        themeToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTheme();
            }
        });
    }

    private void startNewQuiz() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    private void finishApp() {
        finishAffinity();
        System.exit(0);
    }
    private void toggleTheme() {
        ThemeHelper.toggleTheme(this);
        recreate();
    }
    private void updateThemeToggleIcon() {
        if (ThemeHelper.isDarkMode(this)) {
            themeToggleButton.setText("☀️");
        } else {
            themeToggleButton.setText("🌙");
        }
    }
}