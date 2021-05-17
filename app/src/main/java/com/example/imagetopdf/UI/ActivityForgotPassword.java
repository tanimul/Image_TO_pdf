package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.imagetopdf.databinding.ActivityForgotPasswordBinding;

public class ActivityForgotPassword extends AppCompatActivity {
    private static final String TAG = "ActivityForgotPassword";
    private ActivityForgotPasswordBinding activityForgotPasswordBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForgotPasswordBinding=ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(activityForgotPasswordBinding.getRoot());
    }
}