package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

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


        activitySettingBinding.switchActSettingDartMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "Dart Mode. ");
                    Toast.makeText(ActivitySetting.this, "Dart Mode.", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(TAG, "Day Mode. ");
                    Toast.makeText(ActivitySetting.this, "Day Mode.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activitySettingBinding.switchActSettingAutoUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "Auto Update On. ");
                    Toast.makeText(ActivitySetting.this, "Auto Update On.", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(TAG, "Auto Update Off. ");
                    Toast.makeText(ActivitySetting.this, "Auto Update Off.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}