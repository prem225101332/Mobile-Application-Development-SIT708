package com.example.task_21;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner2);
        button = findViewById(R.id.button1);
    }
    public void handleEnter(View v){
        String selected = spinner.getSelectedItem().toString();

        if (selected.equals("Currency")) {
            Intent intent = new Intent(this, Currency.class);
            startActivity(intent);
        } else if (selected.equals("Fuel Efficiency")) {
            Intent intent = new Intent(this, Fuel.class);
            startActivity(intent);
        } else if (selected.equals("Temperature")) {
            Intent intent = new Intent(this, Temperature.class);
            startActivity(intent);
        }
    }

}