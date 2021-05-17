package com.example.imagetopdf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imagetopdf.UI.ActivityLogin;
import com.example.imagetopdf.databinding.ActivitySignUpBinding;

public class ActivitySignUp extends AppCompatActivity {
    private static final String TAG = "ActivitySignUp";
    private ActivitySignUpBinding activitySignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());

        activitySignUpBinding.imagebuttonSignupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activitySignUpBinding.textviewSingupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(ActivitySignUp.this, ActivityLogin.class));
               finish();
            }
        });
    }
}