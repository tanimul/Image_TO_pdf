package com.example.imagetopdf.UI;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.imagetopdf.ModelClass.ModelUser;
import com.example.imagetopdf.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ActivitySignUp";
    private ActivitySignUpBinding activitySignUpBinding;

    private String userName, userEmail, userPassword, userConfirmPassword;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    ProgressDialog Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("User_Registration");
        firebaseAuth = FirebaseAuth.getInstance();
        Dialog = new ProgressDialog(ActivitySignUp.this);
        Dialog.setMessage("Please wait ...");


        activitySignUpBinding.imagebuttonSignupBack.setOnClickListener(this);
        activitySignUpBinding.textviewSingupLogin.setOnClickListener(this);
        activitySignUpBinding.buttonRegisteredSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == activitySignUpBinding.imagebuttonSignupBack) {
            onBackPressed();
        }
        if (v == activitySignUpBinding.textviewSingupLogin) {
            startActivity(new Intent(ActivitySignUp.this, ActivityLogin.class));
            finish();
        }
        if (v == activitySignUpBinding.buttonRegisteredSignup) {
            Dialog.show();
            if (userRegistrationValidation()) {
                Log.d(TAG, "Validation Successful!!");
                checkUserStatus();
            } else {
                Log.d(TAG, "Validation UnSuccessful!!");
                // Toast.makeText(ActivitySignUp.this, "Please fill the all Information!!", Toast.LENGTH_SHORT).show();
            }
//                startActivity(new Intent(ActivitySignUp.this, ActivityVerification.class));
//                finish();
        }

    }

    public boolean userRegistrationValidation() {
        userName = activitySignUpBinding.edittextRegisteredUsername.getText().toString();
        userEmail = activitySignUpBinding.edittextRegisteredEmail.getText().toString();
        userPassword = activitySignUpBinding.edittextRegisteredPassword.getText().toString();
        userConfirmPassword = activitySignUpBinding.edittextRegisteredConfirmpassword.getText().toString();

        if (userName.isEmpty()) {
            activitySignUpBinding.edittextRegisteredUsername.setError("Enter Your UserName please");
            activitySignUpBinding.edittextRegisteredUsername.requestFocus();
            return false;
        }
        if (userEmail.isEmpty()) {
            activitySignUpBinding.edittextRegisteredEmail.setError("Enter Your Email please");
            activitySignUpBinding.edittextRegisteredEmail.requestFocus();
            return false;
        }

        if (userPassword.isEmpty()) {
            activitySignUpBinding.edittextRegisteredPassword.setError("Enter Your Password please");
            activitySignUpBinding.edittextRegisteredPassword.requestFocus();
            return false;
        }
        if (userConfirmPassword.isEmpty()) {
            activitySignUpBinding.edittextRegisteredConfirmpassword.setError("Enter Your Confirm Password please");
            activitySignUpBinding.edittextRegisteredConfirmpassword.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            activitySignUpBinding.edittextRegisteredEmail.setError("Enter a Valid Email please");
            activitySignUpBinding.edittextRegisteredEmail.requestFocus();
            return false;

        }

        if (userPassword.length() < 6) {
            activitySignUpBinding.edittextRegisteredPassword.setError("Password must be 6 digit");
            activitySignUpBinding.edittextRegisteredPassword.requestFocus();
            return false;
        }
        if (userConfirmPassword.length() < 6) {
            activitySignUpBinding.edittextRegisteredConfirmpassword.setError("Confirm Password must be 6 digit");
            activitySignUpBinding.edittextRegisteredConfirmpassword.requestFocus();
            return false;
        }
        if (!userPassword.equals(userConfirmPassword)) {
            activitySignUpBinding.edittextRegisteredPassword.setError("Password and Confirm Password must be same");
            activitySignUpBinding.edittextRegisteredPassword.requestFocus();
            return false;

        } else {
            return true;
        }
    }

    private void checkUserStatus() {
        Log.d(TAG, "Check User.");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ModelUser modelUser = dataSnapshot.getValue(ModelUser.class);
                        if (!modelUser.getUserEmail().equals(userEmail)) {
                            Log.d(TAG, "New User.");
                            userRegistration();
                        } else {
                            Log.d(TAG, "Existing User.");
                            Toast.makeText(ActivitySignUp.this, "Already Registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.d(TAG, "Null Database Reference .");
                    userRegistration();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Dialog.dismiss();
                Toast.makeText(ActivitySignUp.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Database error. " + error.getMessage());
            }
        });
    }

    private void userRegistration() {

        String key = databaseReference.getKey();

        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUser modelUser = new ModelUser(userName, userEmail);
                databaseReference.setValue(modelUser);
                Log.d(TAG, "New User added in Firebase Real time Database.");
//                Toast.makeText(ActivitySignUp.this, "User Added Successfully", Toast.LENGTH_SHORT).show();
                sendEmailVerification();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Dialog.dismiss();
                Toast.makeText(ActivitySignUp.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Database error. " + error.getMessage());
            }
        });


    }

    private void sendEmailVerification() {

        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Dialog.dismiss();
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ActivitySignUp.this, "Verification link sent to Your Email.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Verification link sent.");
                                    startActivity(new Intent(ActivitySignUp.this, ActivityLogin.class));
                                    finish();
                                }


                            });
                        } else {
                            Dialog.dismiss();
                            Toast.makeText(ActivitySignUp.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Something is wrong. " + task.getException());
                        }
                    }
                });
    }
}