package com.example.task_21;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Fuel extends AppCompatActivity {

    Spinner spinner4, spinner5;
    EditText input;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fuel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner4 = findViewById(R.id.spinner4);
        spinner5 = findViewById(R.id.spinner5);
        input = findViewById(R.id.editTextText2);
        result = findViewById(R.id.textView10);
    }
    public void goHome(View v) {
        finish();
    }
    public void calculateFuel(View V){
        String from = spinner4.getSelectedItem().toString();
        String to = spinner5.getSelectedItem().toString();
        double amount = Double.parseDouble(input.getText().toString());
        double converted = 0;

        if (from.equals("mpg") && to.equals("Km/L")) {
            converted = amount * 0.425;
        } else if (from.equals("Km/L") && to.equals("mpg")) {
            converted = amount / 0.425;
        } else if (from.equals("mpg") && to.equals("Km/L")) {
            converted = amount * 0.425;
        } else if (from.equals("Km/L") && to.equals("mpg")) {
            converted = amount / 0.425;
        } else if (from.equals("Gallon(US)") && to.equals("Litres")) {
            converted = amount * 3.785;
        } else if (from.equals("Litres") && to.equals("Gallon(US)")) {
            converted = amount / 3.785;
        } else if (from.equals("Nautical Mile") && to.equals("Km")) {
            converted = amount * 1.852;
        } else if (from.equals("Km") && to.equals("Nautical Mile")) {
            converted = amount / 1.852;
        }
        result.setText(amount + " " + from + " = " + converted + " " + to);
    }

}