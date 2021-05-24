package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.imagetopdf.databinding.ActivityTrashBinding;

public class ActivityTrash extends AppCompatActivity {
    private static final String TAG = "ActivityTrash";
    private ActivityTrashBinding activityTrashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTrashBinding=ActivityTrashBinding.inflate(getLayoutInflater());
        setContentView(activityTrashBinding.getRoot());

    }
}