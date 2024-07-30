package com.example.ritik_1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EmergencyPetrol extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_petrol);

        // Initialize button for calling petrol delivery service
        Button btnCallForPetrol = findViewById(R.id.btnCallForPetrol);

        // Set click listener for calling petrol delivery service
        btnCallForPetrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the petrol delivery service
                callPetrolDeliveryService();
            }
        });
    }

    // Method to initiate a phone call to the petrol delivery service
    private void callPetrolDeliveryService() {
        // Replace the number below with the actual petrol delivery service number
        String phoneNumber = "1800-123-4567";

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
