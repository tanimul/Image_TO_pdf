package com.example.imagetopdf;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.imagetopdf.UI.ActivityHome;

public class ProgressBarAnimation extends Animation {
    private Context context;
    private TextView textView;
    private ProgressBar progressBar;
    private float from;
    private float to;

    public ProgressBarAnimation(Context context, TextView textView, ProgressBar progressBar, float from, float to) {
        this.context = context;
        this.textView = textView;
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
        textView.setText("" + value);

//        if (value == to) {
//            context.startActivity(new Intent(context, ActivityHome.class));
//        }

    }
}
