package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText nameInput;
    private Button startQuizButton;
    private Button themeToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.etPlayerName);
        startQuizButton = findViewById(R.id.btnStartQuiz);
        themeToggleButton = findViewById(R.id.btnThemeToggle);

        String savedName = ThemeHelper.getSavedPlayerName(this);
        if (!savedName.isEmpty()) {
            nameInput.setText(savedName);
        }

        updateThemeToggleIcon();
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        themeToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTheme();
            }
        });
    }

    private void startQuiz() {
        String playerName = nameInput.getText().toString().trim();

        if (playerName.isEmpty()) {
            Toast.makeText(this, R.string.error_name_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        ThemeHelper.savePlayerName(this, playerName);
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("player_name", playerName);
        startActivity(intent);
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