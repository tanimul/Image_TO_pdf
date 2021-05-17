package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.imagetopdf.databinding.ActivityLetsStartBinding;

public class ActivityLetsStart extends AppCompatActivity {
    private static final String TAG = "ActivityLetsStart";
    private ActivityLetsStartBinding activityLetsStartBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLetsStartBinding=ActivityLetsStartBinding.inflate(getLayoutInflater());
        setContentView(activityLetsStartBinding.getRoot());
    }
}