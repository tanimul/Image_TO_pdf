package com.example.imagetopdf.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.imagetopdf.ModelClass.ModelUser;
import com.example.imagetopdf.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ActivityLogin";
    private ActivityLoginBinding activityLoginBinding;
    private String userEmail, userPassword;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    ProgressDialog Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("User_Registration");
        firebaseAuth = FirebaseAuth.getInstance();
        Dialog = new ProgressDialog(ActivityLogin.this);
        Dialog.setMessage("Please wait ...");

        activityLoginBinding.imagebuttonLoginBack.setOnClickListener(this);
        activityLoginBinding.buttonLoginSignin.setOnClickListener(this);
        activityLoginBinding.textviewLoginSignup.setOnClickListener(this);
        activityLoginBinding.textviewLoginForgotpass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == activityLoginBinding.imagebuttonLoginBack) {
            onBackPressed();
        }
        if (v == activityLoginBinding.buttonLoginSignin) {
            if (userLoginValidation()) {
                Log.d(TAG, "Validation Successful.");
                checkUserInformation();
            }
        }
        if (v == activityLoginBinding.textviewLoginForgotpass) {
            startActivity(new Intent(ActivityLogin.this, ActivityForgotPassword.class));
            finish();
        }
        if (v == activityLoginBinding.textviewLoginSignup) {
            startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
            finish();
        }
    }

    private boolean userLoginValidation() {
        userEmail = activityLoginBinding.edittextLoginEmail.getText().toString();
        userPassword = activityLoginBinding.edittextLoginPassword.getText().toString();
        if (!userEmail.isEmpty() || !userPassword.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(activityLoginBinding.edittextLoginEmail.getText().toString()).matches()) {
                Log.d(TAG, "Valid Email.");
                return true;
            } else {
                Toast.makeText(this, "Please enter a Valid Email please", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Enter a Valid Email please.");
                return false;
            }
        } else {
            Toast.makeText(this, "Please fill the all Information properly", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Please fill the all Information properly.");
            return false;
        }
    }

    private void checkUserInformation() {
        Log.d(TAG, "Check User Information.");
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                Log.d(TAG, "Login Successfully.");
                                Toast.makeText(ActivityLogin.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "Please Verify your Email address.");
                                Toast.makeText(ActivityLogin.this, "Please Verify your Email address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Login Unsuccessfully" + task.getException().toString());
                            Toast.makeText(ActivityLogin.this, "Email or Password isn't Correct", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

}