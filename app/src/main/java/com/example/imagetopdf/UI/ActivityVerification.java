package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.imagetopdf.databinding.ActivityVerificationBinding;

public class ActivityVerification extends AppCompatActivity {
    public static final String TAG = "ActivityVerification";
    private ActivityVerificationBinding activityVerificationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVerificationBinding=ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(activityVerificationBinding.getRoot());
    }
}