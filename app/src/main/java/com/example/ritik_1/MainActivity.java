package com.example.ritik_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView Home, Emergency, sos, contacts, settings, logout;

    public FloatingActionButton user_profile;
    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linear_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton FloatingActionButtonuser_profile = findViewById(R.id.user_profile);
        CardView cardViewHome = findViewById(R.id.home);
        CardView cardViewEmergency = findViewById(R.id.Emergency);
        CardView cardViewSoS = findViewById(R.id.sos);
        CardView cardViewContacts = findViewById(R.id.contacts);
        CardView cardViewSettings = findViewById(R.id.settings);
        auth = FirebaseAuth.getInstance();
        CardView cardViewlogout = findViewById(R.id.logout);
        textView = findViewById(R.id.user_detail);
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
            finish();
        }
        else {
            textView.setText(user.getDisplayName());
        }


        //Intent for Home
        cardViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open MainActivity2
                Intent intent = new Intent(MainActivity.this, SelectGenderActivity.class);
                startActivity(intent);
            }
        });

        //Intent for Profile
        cardViewEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open MainActivity2
                Intent intent = new Intent(MainActivity.this, EmergencyActivity.class);
                startActivity(intent);
            }
        });

        //Intent for SoS
        cardViewSoS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open MainActivity2
                Intent intent = new Intent(MainActivity.this, SoS.class);
                startActivity(intent);
            }
        });

        cardViewContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open MainActivity2
                Intent intent = new Intent(MainActivity.this, Contacts.class);
                startActivity(intent);
            }
        });

        cardViewlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });

        cardViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the maps activity
                Intent intent = new Intent(MainActivity.this, NearbyPlacesActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButtonuser_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}