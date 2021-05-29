package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imagetopdf.databinding.ActivityForgotPasswordBinding;

public class ActivityForgotPassword extends AppCompatActivity {
    private static final String TAG = "ActivityForgotPassword";
    private ActivityForgotPasswordBinding activityForgotPasswordBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForgotPasswordBinding=ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(activityForgotPasswordBinding.getRoot());

        activityForgotPasswordBinding.buttonForgotpasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityForgotPassword.this, ActivityVerification.class));
                finish();
            }
        });
    }
}