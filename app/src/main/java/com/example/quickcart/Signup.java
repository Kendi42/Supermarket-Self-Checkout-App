package com.example.quickcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickcart.ui.main.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    // Declarations
    Button btn_signup;
    TextView tv_loginInstead;
    EditText et_email, et_password, et_conPassword, et_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        // Binding

        tv_loginInstead = findViewById(R.id.tv_loginInstead);
        et_email = findViewById(R.id.et_emailSignup);
        et_password = findViewById(R.id.et_passwordSignup);
        et_conPassword = findViewById(R.id.et_confirm);
        et_username = findViewById(R.id.et_username);
        btn_signup = findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        // Go back to Log in Page
        tv_loginInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });


        // hide the status bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
    }

    private void signup() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String cpassword = et_conPassword.getText().toString().trim();
        String username = et_username.getText().toString().trim();
        String phone = "unset";

        if(username.isEmpty()){
            et_username.setError("Username is required!");
            et_username.requestFocus();
            return;
        }

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
        if (cpassword.isEmpty()) {
            et_conPassword.setError("Confirmation password is required");
            et_conPassword.requestFocus();
            return;
        }
        if (cpassword.length() < 6) {
            et_conPassword.setError("Min password length should be 6 characters");
            et_conPassword.requestFocus();
            return;
        }
        if(!cpassword.equals(password)){
            et_conPassword.setError("Password does not match");
            et_conPassword.requestFocus();
        }

        else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(username, email, phone);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Signup succesful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Signup.this, Homepage.class);
                                    startActivity(intent);
                                    //DatabaseReference userRef = database.getReference("Users").child(email).child();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Sign up failed: " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Sign up failed: " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }
}