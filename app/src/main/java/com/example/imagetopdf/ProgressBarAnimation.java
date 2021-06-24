package com.example.imagetopdf;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.imagetopdf.UI.ActivityHome;
import com.example.imagetopdf.UI.ActivityLetsStart;

public class ProgressBarAnimation extends Animation {
    private static final String TAG = "Progressbar animation";
    private Context context;
    private TextView textView, textView2;
    private ProgressBar progressBar;
    private float from;
    private float to;
    private Button button;

    public ProgressBarAnimation(Context context, TextView textView, TextView textView2,Button button, ProgressBar progressBar, float from, float to) {
        this.context = context;
        this.textView = textView;
        this.textView2 = textView2;
        this.button=button;
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
        textView2.setText((int) value + " %");
        Log.d(TAG, "Progress: " + (int) value);
        if (value <= 50) {
            textView.setText("Please Wait : ");
        } else if (value < 100 && value > 50) {
            textView.setText("Almost Complete : ");
        } else {
            textView.setText("Complete : ");
            button.setVisibility(View.VISIBLE);
        }


    }
}
