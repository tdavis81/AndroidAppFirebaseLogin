package com.example.xxtyl.southhillschatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button forgotPassword;
    private AutoCompleteTextView mEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_reset_pass);

       progressBar = (ProgressBar) findViewById(R.id.progress);

       forgotPassword = (Button) findViewById(R.id.resetPass);
       auth = FirebaseAuth.getInstance();
       forgotPassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String email = mEmail.getText().toString().trim();
               if(TextUtils.isEmpty(email)){
                   Toast.makeText(getBaseContext(),"Please enter your email to reset password.",Toast.LENGTH_SHORT).show();
                   return;
               }
                progressBar.setVisibility(View.VISIBLE);
               auth.sendPasswordResetEmail(email)
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   Toast.makeText(getBaseContext(),"We have sent you instructions to reset your password!",Toast.LENGTH_SHORT).show();
                               } else {
                                   Toast.makeText(getBaseContext(),"Failed to send reset email..",Toast.LENGTH_SHORT).show();
                               }
                               progressBar.setVisibility(View.GONE);
                           }
                       });
           }
       });

    }
}
