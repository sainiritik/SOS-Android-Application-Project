package com.example.ritik_1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EmergencyAccident extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_accident);

        // Initialize buttons
        Button btnFire = findViewById(R.id.btnFire);
        Button btnAmbulance = findViewById(R.id.btnAmbulance);
        Button btnPolice = findViewById(R.id.btnPolice);
        Button btnRoadDepartment = findViewById(R.id.btnRoadDepartment);

        // Set click listeners for emergency service buttons
        btnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEmergencyNumber("7409556553"); // Fire Department
            }
        });

        btnAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEmergencyNumber("7409556553"); // Ambulance
            }
        });

        btnPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEmergencyNumber("7409556553"); // Police
            }
        });

        btnRoadDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEmergencyNumber("7409556553"); // Road Department
            }
        });
    }

    // Method to initiate a phone call to the given emergency number
    private void callEmergencyNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
