package com.example.ritik_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EmergencyActivity extends AppCompatActivity {

    CardView Accicardview, Firecardview, Petrolcardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emergency);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.emergencyActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        Accicardview = findViewById(R.id.acciCardView);
        Petrolcardview = findViewById(R.id.petrolCardView);
        Firecardview = findViewById(R.id.fireCardView);


        Accicardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyActivity.this, EmergencyAccident.class);
                startActivity(intent);
            }
        });

        Petrolcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyActivity.this,EmergencyPetrol.class);
                startActivity(intent);
            }
        });

        Firecardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyActivity.this, FireActivity.class);
                startActivity(intent);
            }
        });

    }
}