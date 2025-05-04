package com.example.ritik_1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ritik_1.logic.LoginTest;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DURATION = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity or any other activity after the splash screen duration
                Intent intent = new Intent(SplashScreenActivity.this, LoginTest.class);
                startActivity(intent);
                finish(); // Finish the splash screen activity to prevent going back
            }
        }, SPLASH_SCREEN_DURATION);
    }
}
