package com.example.ritik_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SelectGenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gender);

        // Initialize views
        RadioGroup radioGroupGender = findViewById(R.id.radioGroupGender);
        Button btnNext = findViewById(R.id.btnNext);

        // Set click listener for the Next button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected gender
                int selectedId = radioGroupGender.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);

                // Check if a gender is selected
                if (selectedId != -1) {
                    // Navigate to the corresponding activity based on the selected gender
                    if (radioButton.getText().toString().equals("Male")) {
                        startActivity(new Intent(SelectGenderActivity.this, MaleActivity.class));
                    } else {
                        startActivity(new Intent(SelectGenderActivity.this, FemaleSafety.class));
                    }
                }
            }
        });
    }
}
