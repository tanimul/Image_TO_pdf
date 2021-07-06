package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.imagetopdf.App;
import com.example.imagetopdf.KEYS;
import com.example.imagetopdf.Tools;
import com.example.imagetopdf.databinding.ActivitySettingBinding;


public class ActivitySetting extends AppCompatActivity {
    private static final String TAG = "ActivitySetting";
    private ActivitySettingBinding activitySettingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(activitySettingBinding.getRoot());

        checkDayNightMode();
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
                    Log.d(TAG, "Checked");
                    Tools.savePrefBoolean(KEYS.IsDartMode, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    Log.d(TAG, "Unchecked");
                    Tools.savePrefBoolean(KEYS.IsDartMode, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    private void checkDayNightMode() {
        if (Tools.getPrefBoolean(KEYS.IsDartMode, true)) {
            activitySettingBinding.switchActSettingDartMode.setChecked(true);
        } else {
            activitySettingBinding.switchActSettingDartMode.setChecked(false);
        }
    }
}