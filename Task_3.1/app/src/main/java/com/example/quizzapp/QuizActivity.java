package com.example.quizzapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.util.List;


public class QuizActivity extends AppCompatActivity {

    private TextView questionText;
    private TextView counterText;
    private TextView percentText;
    private LinearProgressIndicator progressBar;

    private MaterialButton option0;
    private MaterialButton option1;
    private MaterialButton option2;
    private MaterialButton option3;
    private MaterialButton submitButton;
    private Button themeToggleButton;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String playerName;

    private int selectedOptionIndex = -1;
    private boolean isSubmitted = false;


    private MaterialButton[] optionButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        playerName = getIntent().getStringExtra("player_name");

        questionText = findViewById(R.id.tvQuestion);
        counterText = findViewById(R.id.tvQuestionCounter);
        percentText = findViewById(R.id.tvProgressPercent);
        progressBar = findViewById(R.id.progressBar);
        option0 = findViewById(R.id.btnOption0);
        option1 = findViewById(R.id.btnOption1);
        option2 = findViewById(R.id.btnOption2);
        option3 = findViewById(R.id.btnOption3);
        submitButton = findViewById(R.id.btnSubmit);
        themeToggleButton = findViewById(R.id.btnThemeToggle);

        optionButtons = new MaterialButton[]{option0, option1, option2, option3};
        questions = QuizBank.getQuestions();
        submitButton.setEnabled(false);
        loadCurrentQuestion();
        setupClickListeners();
        updateThemeToggleIcon();
    }
    private void loadCurrentQuestion() {
        Question q = questions.get(currentIndex);
        int totalQuestions = questions.size();

        isSubmitted = false;
        selectedOptionIndex = -1;
        submitButton.setEnabled(false);
        submitButton.setText(getString(R.string.btn_submit));

        questionText.setText(q.getEmoji() + "\n\n" + q.getText());
        String[] letters = {"A", "B", "C", "D"};
        List<String> options = q.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(letters[i] + ".  " + options.get(i));
            resetOptionButton(optionButtons[i]);
            optionButtons[i].setClickable(true);
            optionButtons[i].setAlpha(1f);
        }

        int percent = (currentIndex * 100) / totalQuestions;
        counterText.setText("Question " + (currentIndex + 1) + " of " + totalQuestions);
        percentText.setText(percent + "%");
        progressBar.setProgressCompat(percent, true);
    }

    private void setupClickListeners() {
        for (int i = 0; i < optionButtons.length; i++) {
            final int index = i;
            optionButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSubmitted) {
                        selectOption(index);
                    }
                }
            });
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSubmitted) {
                    submitAnswer();
                } else {
                    advanceToNext();
                }
            }
        });

        themeToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeHelper.toggleTheme(QuizActivity.this);
                recreate();
            }
        });
    }

    private void selectOption(int index) {
        if (selectedOptionIndex != -1) {
            resetOptionButton(optionButtons[selectedOptionIndex]);
        }

        selectedOptionIndex = index;

        optionButtons[index].setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_option_selected_bg))
        );
        optionButtons[index].setStrokeColor(
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_option_selected_border))
        );
        optionButtons[index].setTextColor(
                ContextCompat.getColor(this, R.color.color_primary)
        );

        submitButton.setEnabled(true);
    }

    private void submitAnswer() {
        isSubmitted = true;
        Question currentQuestion = questions.get(currentIndex);

        if (selectedOptionIndex == currentQuestion.getCorrectIndex()) {
            score++;
        }
        for (int i = 0; i < optionButtons.length; i++) {
            if (i == currentQuestion.getCorrectIndex()) {
                optionButtons[i].setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_correct))
                );
                optionButtons[i].setTextColor(Color.WHITE);
            } else if (i == selectedOptionIndex) {
                optionButtons[i].setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_wrong))
                );
                optionButtons[i].setTextColor(Color.WHITE);
            } else {
                optionButtons[i].setAlpha(0.4f);
            }
            optionButtons[i].setClickable(false);
        }

        int total = questions.size();
        int percent = ((currentIndex + 1) * 100) / total;
        percentText.setText(percent + "%");
        progressBar.setProgressCompat(percent, true);

        boolean isLast = (currentIndex == total - 1);
        if (isLast) {
            submitButton.setText(getString(R.string.btn_see_results));
        } else {
            submitButton.setText(getString(R.string.btn_next));
        }
    }

    private void advanceToNext() {
        boolean isLast = (currentIndex == questions.size() - 1);
        if (isLast) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("player_name", playerName);
            intent.putExtra("score", score);
            intent.putExtra("total", questions.size());
            startActivity(intent);
            finish();
        } else {
            currentIndex++;
            loadCurrentQuestion();
        }
    }
    private void resetOptionButton(MaterialButton button) {
        button.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_option_bg))
        );
        button.setStrokeColor(
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_option_border))
        );
        button.setTextColor(
                ContextCompat.getColor(this, R.color.color_option_text)
        );
        button.setAlpha(1f);
    }
    private void updateThemeToggleIcon() {
        if (ThemeHelper.isDarkMode(this)) {
            themeToggleButton.setText("☀️");
        } else {
            themeToggleButton.setText("🌙");
        }
    }
}