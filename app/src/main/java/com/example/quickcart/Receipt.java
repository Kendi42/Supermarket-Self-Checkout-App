package com.example.quickcart;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.quickcart.ui.main.CartModel;
import com.example.quickcart.ui.main.ReceiptModel;
import com.example.quickcart.ui.main.RecyclerViewAdapter;
import com.example.quickcart.ui.main.RecyclerViewAdapterReceipt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Receipt extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<ReceiptModel> receiptList= new ArrayList<>();
    TextView backfromReceipts;

    private FirebaseAuth mAuth;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        backfromReceipts= findViewById(R.id.backFromReceipts);
        backfromReceipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Receipt.this, Homepage.class);
                startActivity(intent);
            }
        });

        DatabaseReference receiptRef = database.getReference("Receipts").child(currentUserID);
        RecyclerView rv_receiptDates = findViewById(R.id.recyclerView_receipt);
        receiptRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ReceiptModel receipt = snapshot.getValue(ReceiptModel.class);
                receiptList.add(receipt);
                rv_receiptDates.setAdapter(new RecyclerViewAdapterReceipt(receiptList, getApplicationContext(), currentUserID));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ReceiptModel receipt = snapshot.getValue(ReceiptModel.class);
                receiptList.add(receipt);
                rv_receiptDates.setAdapter(new RecyclerViewAdapterReceipt(receiptList, getApplicationContext(), currentUserID));

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ReceiptModel receipt = snapshot.getValue(ReceiptModel.class);
                receiptList.add(receipt);
                rv_receiptDates.setAdapter(new RecyclerViewAdapterReceipt(receiptList, getApplicationContext(), currentUserID));

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ReceiptModel receipt = snapshot.getValue(ReceiptModel.class);
                receiptList.add(receipt);
                rv_receiptDates.setAdapter(new RecyclerViewAdapterReceipt(receiptList, getApplicationContext(), currentUserID));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rv_receiptDates.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

    }
}