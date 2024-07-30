package com.example.ritik_1;// EmergencyActivity.java

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FemaleSafety extends AppCompatActivity {

    // Placeholder for the saved emergency number
    private String savedEmergencyNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_safety);
    }

    // Method to call women safety
    public void onWomenSafetyButtonClick(View view) {
        // Replace "women safety number" with the actual women safety contact number
        String womenSafetyNumber = "7409556553";
        callNumber(womenSafetyNumber);
    }

    // Method to call police
    public void onPoliceButtonClick(View view) {
        // Replace "police number" with the actual police contact number
        String policeNumber = "7409556553";
        callNumber(policeNumber);
    }

    // Method to save an emergency number
    /*public void onSaveNumberButtonClick(View view) {
        // For demonstration purposes, let's assume the user enters the number directly here
        savedEmergencyNumber = "7409556553";
        Toast.makeText(this, "Emergency number saved!", Toast.LENGTH_SHORT).show();
    }

    // Method to call the saved emergency number
    public void onCallSavedNumberButtonClick(View view) {
        if (savedEmergencyNumber != null) {
            callNumber(savedEmergencyNumber);
        } else {
            Toast.makeText(this, "No emergency number saved", Toast.LENGTH_SHORT).show();
        }
    }*/

    // Method to initiate a phone call
    private void callNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
