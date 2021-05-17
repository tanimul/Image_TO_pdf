package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.imagetopdf.databinding.ActivityNewPasswordBinding;

public class ActivityNewPassword extends AppCompatActivity {
    private static final String TAG = "ActivityNewPassword";
    private ActivityNewPasswordBinding activityNewPasswordBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNewPasswordBinding=ActivityNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(activityNewPasswordBinding.getRoot());
    }
}