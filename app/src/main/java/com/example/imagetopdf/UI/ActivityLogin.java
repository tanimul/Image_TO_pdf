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

import com.example.imagetopdf.KEYS;
import com.example.imagetopdf.ModelClass.ModelUser;
import com.example.imagetopdf.R;
import com.example.imagetopdf.Tools;
import com.example.imagetopdf.databinding.ActivityLoginBinding;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ActivityLogin";
    private ActivityLoginBinding activityLoginBinding;
    private String userEmail, userPassword;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    //    ProgressDialog Dialog;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("User_Registration");
        firebaseAuth = FirebaseAuth.getInstance();
//        Dialog = new ProgressDialog(ActivityLogin.this);
//        Dialog.setMessage("Please wait ...");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        activityLoginBinding.imagebuttonLoginBack.setOnClickListener(this);
        activityLoginBinding.buttonLoginSignin.setOnClickListener(this);
        activityLoginBinding.textviewLoginSignup.setOnClickListener(this);
        activityLoginBinding.textviewLoginForgotpass.setOnClickListener(this);
        activityLoginBinding.imageviewLoginGoogle.setOnClickListener(this);
        activityLoginBinding.imageviewLoginFacebook.setOnClickListener(this);

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
        if (v == activityLoginBinding.imageviewLoginGoogle) {
            Log.d(TAG, "Google Login Clicked.");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        if (v == activityLoginBinding.imageviewLoginFacebook) {
            Log.d(TAG, "Facebook Login Clicked.");

        }

    }

    private boolean userLoginValidation() {
        userEmail = activityLoginBinding.edittextLoginEmail.getText().toString();
        userPassword = activityLoginBinding.edittextLoginPassword.getText().toString();
        if (!userEmail.isEmpty() || !userPassword.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(activityLoginBinding.edittextLoginEmail.getText().toString()).matches()) {
                Log.d(TAG, "Vali" +
                        "d Email.");
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
                                getUserInformation(task.getResult().getUser().getUid());
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

    public void getUserInformation(String userId) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelUser user = snapshot.getValue(ModelUser.class);
                    Tools.savePref(KEYS.USER_NAME, user.getUserName());
                    if (activityLoginBinding.checkboxLoginCheckRemember.isChecked()) {
                        Log.d(TAG, "Checked.");
                        Tools.savePrefBoolean(KEYS.IS_LOGGED_IN, true);
                    } else {
                        Log.d(TAG, "Unchecked.");
                        Tools.savePrefBoolean(KEYS.IS_LOGGED_IN, false);
                    }
                    startActivity(new Intent(ActivityLogin.this, ActivityLetsStart.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: database error" + error.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogleID: " + account.getId());
                Log.d(TAG, "firebaseAuthWithGoogleEMAIL: " + account.getEmail());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed: " + e.getMessage());

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        Log.d(TAG, "" + idToken);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            firebaseUser = firebaseAuth.getCurrentUser();
                            Log.d(TAG, "User information" + firebaseUser);
                            updateUI(firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser) {

        Log.d(TAG, "acc: " + firebaseUser.getEmail());

        if (firebaseUser != null) {
            String userid = firebaseUser.getUid();
            String personname = firebaseUser.getDisplayName();
            String personemail = firebaseUser.getEmail();


            ModelUser user = new ModelUser(personemail, personname);
            databaseReference.child(userid)
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "adding successfully in Database.");
                    Tools.savePref(KEYS.USER_NAME, personname);
                    Tools.savePrefBoolean(KEYS.IS_LOGGED_IN, true);
                    startActivity(new Intent(ActivityLogin.this, ActivityLetsStart.class));
                    finish();
                }

            });

        } else {
            Log.d(TAG, "Something in Database.");
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
    }


}