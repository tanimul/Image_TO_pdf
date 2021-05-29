package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imagetopdf.databinding.ActivityVerificationBinding;

public class ActivityVerification extends AppCompatActivity {
    public static final String TAG = "ActivityVerification";
    private ActivityVerificationBinding activityVerificationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVerificationBinding=ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(activityVerificationBinding.getRoot());


        activityVerificationBinding.buttonVerificationContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityVerification.this, ActivityLetsStart.class));
                finish();

                //if exist user than go set new password
            }
        });
    }
}