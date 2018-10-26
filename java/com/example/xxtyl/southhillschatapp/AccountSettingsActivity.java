package com.example.xxtyl.southhillschatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountSettingsActivity extends AppCompatActivity {

    private Button changePass, signOut;
    private EditText oldPass,newPass;
    private AutoCompleteTextView email;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        setTitle("Account Settings");
        auth = FirebaseAuth.getInstance();
        email = (AutoCompleteTextView) findViewById(R.id.email);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null) {
                    Intent myIntent = new Intent(getBaseContext(), AccountSettingsActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        };

        signOut = (Button) findViewById(R.id.signOut);
        changePass = (Button) findViewById(R.id.changePassword);
        oldPass = (EditText) findViewById(R.id.oldPassword);
        newPass = (EditText) findViewById(R.id.newPassword);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        if(progressBar !=null) {
            progressBar.setVisibility(View.GONE);
        }

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if(user!=null && !newPass.getText().toString().trim().equals("")) {
                    if(newPass.getText().toString().trim().length() <6) {
                        newPass.setError("Password to short, enter minimum of 6 characters.");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPass.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(AccountSettingsActivity.this,"Password updated, you can now sign in with your new password.", Toast.LENGTH_SHORT).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(AccountSettingsActivity.this,"Failed to update password.", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (newPass.getText().toString().trim().equals("")){
                    newPass.setError("Enter password!");
                    progressBar.setVisibility(View.GONE);
                }
            }


        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user){
        email.setText("User Email:" + user.getEmail());
    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user ==null){
                Intent myIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(myIntent);
                finish();
            } else {
                setDataToView(user);
                //Load image
                /*
                if(user.getPhotoUrl() !=null){
                    Log.d("Account","PhotoURL: " + user.getPhotoUrl());
                    //Picasso.with(AccDetail.this).load(user.getPhotoUrl()).into(imageView);
                }
                */
            }

        }
    };

    public void signOut() {
        auth.signOut();


        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user ==null){
                    Intent myIntent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }
}

