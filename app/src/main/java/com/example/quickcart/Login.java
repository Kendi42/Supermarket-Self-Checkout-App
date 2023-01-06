package com.example.quickcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private Button btn_log;
    private EditText et_password,et_email;
    private TextView tv_show, tv_forgot, tv_signupInstead;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        // BINDING
        btn_log = (Button) findViewById(R.id.btn_log);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_forgot = (TextView) findViewById(R.id.tv_forgot);
        tv_signupInstead = (TextView) findViewById(R.id.tv_signupInstead);

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               userlogin();
            }
        });

        // Don't have an Account, Sign up instead
        tv_signupInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);

            }
        });

        // hide the status bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
    }

    private void userlogin() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if(email.isEmpty()){
            et_email.setError("Email is required!");
            et_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError(("Please enter a valid email"));
            et_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            et_password.setError("Password is required");
            et_password.requestFocus();
            return;
        }
        if(password.length() < 6){
            et_password.setError("Min password length is 6 characters!");
            et_password.requestFocus();
            return;
        }
        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Log in succesful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Homepage.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Log in failed: " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }}});}}}