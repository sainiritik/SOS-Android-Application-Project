package com.example.ritik_1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class FireActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire);

        Button btnCallFire = findViewById(R.id.btnCallFire);
        Button btnCallAmbulance = findViewById(R.id.btnCallAmbulance);
        Button btnCallPolice = findViewById(R.id.btnCallPolice);

        btnCallFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the fire department
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7409556553"));
                startActivity(intent);
            }
        });

        btnCallAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call an ambulance
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7409556553"));
                startActivity(intent);
            }
        });

        btnCallPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the police
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7409556553"));
                startActivity(intent);
            }
        });
    }
}
