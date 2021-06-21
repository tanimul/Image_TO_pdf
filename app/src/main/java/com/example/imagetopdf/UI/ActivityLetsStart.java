package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imagetopdf.ProgressBarAnimation;
import com.example.imagetopdf.databinding.ActivityLetsStartBinding;

public class ActivityLetsStart extends AppCompatActivity {
    private static final String TAG = "ActivityLetsStart";
    private ActivityLetsStartBinding activityLetsStartBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLetsStartBinding = ActivityLetsStartBinding.inflate(getLayoutInflater());
        setContentView(activityLetsStartBinding.getRoot());


        activityLetsStartBinding.progressbarLetsstart.setMax(100);
        activityLetsStartBinding.progressbarLetsstart.setScaleY(2f);
        progressBarAnimation();

        activityLetsStartBinding.buttonLetstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLetsStart.this, ActivityHome.class));
                finish();
            }
        });
    }

    private void progressBarAnimation() {
        ProgressBarAnimation progressBarAnimation = new ProgressBarAnimation(this
                , activityLetsStartBinding.textviewLetstartPercentage
                , activityLetsStartBinding.progressbarLetsstart, 0f, 100f);
        activityLetsStartBinding.progressbarLetsstart.setAnimation(progressBarAnimation);
    }
}