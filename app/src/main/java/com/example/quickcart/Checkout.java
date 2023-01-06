package com.example.quickcart;

import static android.content.ContentValues.TAG;

import static com.example.quickcart.Constants.BUSINESS_SHORT_CODE;
import static com.example.quickcart.Constants.CALLBACKURL;
import static com.example.quickcart.Constants.PARTYB;
import static com.example.quickcart.Constants.PASSKEY;
import static com.example.quickcart.Constants.TRANSACTION_TYPE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickcart.Model.AccessToken;
import com.example.quickcart.Model.STKPush;
import com.example.quickcart.Services.DarajaAPiClient;
import com.example.quickcart.ui.main.Cart;
import com.example.quickcart.ui.main.CartModel;
import com.example.quickcart.ui.main.ReceiptModel;
import com.example.quickcart.ui.main.RecyclerViewAdapter;
import com.example.quickcart.ui.main.RecyclerViewAdapterCheckout;
import com.example.quickcart.ui.main.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class Checkout extends AppCompatActivity {

    TextView backToCart, tv_totalCheckout, tv_phoneNo, tv_changePhone;
    Button btn_checkout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String totalCheck;
    String totalAmount;
    float t;
    ArrayList<CartModel> checkoutItems= new ArrayList<>();
    private FirebaseAuth mAuth;
    String currentUserID;
    String userPhoneNumber;

    private DarajaAPiClient mApiClient;
    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        tv_phoneNo = findViewById(R.id.tv_phoneNumber);
        DatabaseReference cartref = database.getReference("Carts").child(currentUserID).child("Items");
        DatabaseReference phoneRef = database.getReference("Users").child(currentUserID).child("phonenumber");
        phoneRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userPhoneNumber = snapshot.getValue(String.class);
                tv_phoneNo.setText(userPhoneNumber);
                if(userPhoneNumber.equals("unset")){
                    Dialog dialog = new Dialog(Checkout.this);
                    dialog.setContentView(R.layout.phonenumber_dialog);
                    Button btn_submit;
                    EditText et_phoneNumber;

                    btn_submit =dialog.findViewById(R.id.btn_submitPhone);
                    et_phoneNumber =dialog.findViewById(R.id.editTextPhone);

                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String phoneInput = et_phoneNumber.getText().toString().trim();
                            phoneRef.setValue(phoneInput);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mProgressDialog = new ProgressDialog(this);
        mApiClient = new DarajaAPiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.
        //mPay.setOnClickListener(this);
        getAccessToken();

        tv_changePhone = findViewById(R.id.tv_changePayment);
        tv_changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Checkout.this);
                dialog.setContentView(R.layout.phonenumber_dialog);
                Button btn_submit;
                EditText et_phoneNumber;

                btn_submit =dialog.findViewById(R.id.btn_submitPhone);
                et_phoneNumber =dialog.findViewById(R.id.editTextPhone);

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phoneInput = et_phoneNumber.getText().toString().trim();
                        phoneRef.setValue(phoneInput);
                        Toast.makeText(getApplicationContext(), "Payment phone number changed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        btn_checkout = findViewById(R.id.btn_checkoutConfirmation);
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view== btn_checkout){
                    String phone_number = userPhoneNumber;
                    String amount = totalAmount;
                    performSTKPush(phone_number,amount);

                    new CountDownTimer(30000, 1000) {
                        public void onFinish() {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            Date date = new Date();
                            DatabaseReference receiptRef = database.getReference("Receipts").child(currentUserID).child(formatter.format(date));
                            ReceiptModel receipt = new ReceiptModel(formatter.format(date), totalAmount);
                            receiptRef.setValue(receipt);
                            cartref.addValueEventListener(new ValueEventListener() {
                                int i =0;
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                                        CartModel item = itemSnapshot.getValue(CartModel.class);
                                        receiptRef.child("Items").child(String.valueOf(item.getProductID())).setValue(item);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                        }
                    }.start();
                }
            }
        });

        tv_totalCheckout = findViewById(R.id.tv_totalCheckout);
        DatabaseReference totalref = database.getReference("Carts").child(currentUserID).child("Total");
        totalref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalAmount= String.valueOf(snapshot.getValue());
                totalCheck = "Ksh: " + String.valueOf(snapshot.getValue());
                tv_totalCheckout.setText(totalCheck);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        RecyclerView rv_checkout = findViewById(R.id.rv_checkout);
        cartref.addValueEventListener(new ValueEventListener() {
            int i =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checkoutItems.clear();
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    CartModel item = itemSnapshot.getValue(CartModel.class);
                    Log.d(TAG, "Value is: " + item.toString());
                    checkoutItems.add(item);
                    rv_checkout.setAdapter(new RecyclerViewAdapterCheckout(checkoutItems, getApplicationContext()));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rv_checkout.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        backToCart = findViewById(R.id.backToCart);
        backToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                startActivity(intent);

            }
        });


    }

    public void performSTKPush(String phone_number, String amount) {

        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "QuickCart", //Account reference
                "QuickCart Mobile Purchase"  //Transaction description

        );
        mApiClient.setGetAccessToken(false);
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(Call<STKPush> call, Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());

                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<STKPush> call, Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t);
            }
        });
    }

    public void getAccessToken() {

        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    }

