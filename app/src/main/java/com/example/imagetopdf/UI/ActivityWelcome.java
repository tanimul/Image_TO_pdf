
package com.example.imagetopdf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.imagetopdf.KEYS;
import com.example.imagetopdf.Tools;
import com.example.imagetopdf.databinding.ActivityWelcomeBinding;

import io.andref.rx.network.RxNetwork;
import rx.functions.Action1;


public class ActivityWelcome extends AppCompatActivity {
    private static final String TAG = "Activity_Welcome";
    private ActivityWelcomeBinding activityWelcomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWelcomeBinding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(activityWelcomeBinding.getRoot());


        if (Tools.getPrefBoolean(KEYS.IS_LOGGED_IN, false)) {
            Log.d(TAG, "Logged In");
            startActivity(new Intent(ActivityWelcome.this, ActivityHome.class));
            finish();
        }

        activityWelcomeBinding.textviewMainRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityWelcome.this, ActivitySignUp.class));
            }
        });

        activityWelcomeBinding.buttonMainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityWelcome.this, ActivityLogin.class));
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume!");
        isNetworkConnected();
    }

    //Check network connected or not
    private void isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        RxNetwork.connectivityChanges(this, connectivityManager)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean connected) {
                        if (connected) {
                            Log.d(TAG, "Network Connected!");
                            activityWelcomeBinding.buttonMainLogin.setText("ACCOUNT LOGIN");
                            activityWelcomeBinding.buttonMainLogin.setClickable(true);
                            activityWelcomeBinding.textviewMainRegistration.setVisibility(View.VISIBLE);
                        } else {
                            Log.d(TAG, "Network Not Connected!");
                            Toast.makeText(ActivityWelcome.this, "Please Connect your Internet!!", Toast.LENGTH_SHORT).show();
                            activityWelcomeBinding.buttonMainLogin.setText("No Internet");
                            activityWelcomeBinding.buttonMainLogin.setClickable(false);
                            activityWelcomeBinding.textviewMainRegistration.setVisibility(View.GONE);
                        }
                    }
                });

    }
}