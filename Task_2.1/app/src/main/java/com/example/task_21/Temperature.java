package com.example.task_21;

import android.widget.Toast;
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

public class Temperature extends AppCompatActivity {

    Spinner spinner6, spinner7;
    EditText input;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.temperature);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner6 = findViewById(R.id.spinner6);
        spinner7 = findViewById(R.id.spinner7);
        input = findViewById(R.id.editTextText3);
        result = findViewById(R.id.textView13);
    }
    public void goHome(View v) {
        finish();
    }
    public void calculateTemp(View V){
        String from = spinner6.getSelectedItem().toString();
        String to = spinner7.getSelectedItem().toString();

        String raw = input.getText().toString();

        if (raw.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        if (from.equals(to)) {
            Toast.makeText(this, "Please select different units", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(input.getText().toString());
        double converted = 0;

        if(from.equals("Fahrenheit") && to.equals("Celsius")){
            converted = (amount - 32)/1.8;
        } else if(from.equals("Celsius") && to.equals("Fahrenheit")){
            converted = (amount * 1.8) + 32;
        } else if(from.equals("Celsius") && to.equals("Kelvin")){
            converted = amount + 273.15;
        } else if(from.equals("Kelvin") && to.equals("Celsius")) {
            converted = amount - 273.15;
        } else if (from.equals("Fahrenheit") && to.equals("Kelvin")) {
            double celsius = (amount - 32) / 1.8;
            converted = celsius + 273.15;
        } else if (from.equals("Kelvin") && to.equals("Fahrenheit")) {
            double celsius = amount - 273.15;
            converted = (celsius * 1.8) + 32;
        }

        result.setText(amount + " " + from + " = " + converted + " " + to);
    }
}