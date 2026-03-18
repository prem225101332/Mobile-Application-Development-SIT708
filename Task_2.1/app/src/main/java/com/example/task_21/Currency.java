package com.example.task_21;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Currency extends AppCompatActivity {

    Spinner spinner, spinner3;
    EditText input;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.currency);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner = findViewById(R.id.spinner);
        spinner3 = findViewById(R.id.spinner3);
        input = findViewById(R.id.editTextText);
        result = findViewById(R.id.textView5);
    }

    double getUsdRate(String currency) {
        switch (currency) {
            case "USD": return 1.0;
            case "AUD": return 1.55;
            case "EUR": return 0.92;
            case "JPY": return 148.50;
            case "GBP": return 0.78;
            default:    return 1.0;
        }
    }
    public void goHome(View v) {
        finish();
    }
    public void calculate(View v) {
        String from = spinner.getSelectedItem().toString();
        String to = spinner3.getSelectedItem().toString();

        String raw = input.getText().toString();

        if (raw.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        if (from.equals(to)) {
            Toast.makeText(this, "Please select different units", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(input.getText().toString());
            double inUSD = amount / getUsdRate(from);
            double convertedAmount = inUSD * getUsdRate(to);

            result.setText(amount + " " + from + " = " + convertedAmount + " " + to);
        } catch (NumberFormatException e) {
            result.setText("Invalid input");
        }
    }
}