package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.imagetopdf.databinding.ActivitySettingBinding;

public class ActivitySetting extends AppCompatActivity {
    private static final String TAG = "ActivitySetting";
    private ActivitySettingBinding activitySettingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());

        setContentView(activitySettingBinding.getRoot());

        activitySettingBinding.imagebuttonActSettingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}