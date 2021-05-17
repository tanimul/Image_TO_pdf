package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imagetopdf.ActivitySignUp;
import com.example.imagetopdf.databinding.ActivityWelcomeBinding;

public class ActivityWelcome extends AppCompatActivity {
    private static final String TAG = "Activity_Welcome";
    private ActivityWelcomeBinding activityWelcomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWelcomeBinding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(activityWelcomeBinding.getRoot());

        activityWelcomeBinding.textviewMainRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityWelcome.this, ActivitySignUp.class));
            }
        });

        activityWelcomeBinding.buttonMainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityWelcome.this, ActivityLogin.class));
            }
        });
    }
}