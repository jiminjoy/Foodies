package com.example.foodies;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Start home activity
        startActivity(new Intent(SplashActivity.this, Home.class));
        // close splash activity
        finish();
    }
}
