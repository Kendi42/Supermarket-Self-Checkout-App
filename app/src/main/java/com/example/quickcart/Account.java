package com.example.quickcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickcart.ui.main.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account extends AppCompatActivity {
    TextView backfromAccount, tv_username, tv_useremail, tv_userphone;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        // hide the status bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        tv_username = findViewById(R.id.tv_usernameAccount);
        tv_useremail = findViewById(R.id.tv_useremailAccount);
        tv_userphone = findViewById(R.id.tv_paymentPhone);
        DatabaseReference accountRef = database.getReference("Users").child(currentUserID);

        accountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                tv_username.setText(user.getUsername());
                tv_useremail.setText(user.getEmail());
                tv_userphone.setText(user.getPhonenumber());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        backfromAccount = findViewById(R.id.backFromAccount);
        backfromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Account.this, Homepage.class);
                startActivity(intent);
            }
        });

    }
}