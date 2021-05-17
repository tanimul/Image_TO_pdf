package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imagetopdf.ActivitySignUp;
import com.example.imagetopdf.databinding.ActivityLoginBinding;

public class ActivityLogin extends AppCompatActivity {
    private static final String TAG = "ActivityLogin";
    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        activityLoginBinding.textviewLoginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
                finish();
            }
        });

        activityLoginBinding.textviewLoginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
                finish();
            }
        });

        activityLoginBinding.textviewLoginForgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivityForgotPassword.class));
                finish();
            }
        });

    }
}