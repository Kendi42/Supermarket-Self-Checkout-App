package com.example.quickcart;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.quickcart.ui.main.Cart;
import com.example.quickcart.ui.main.CartModel;
import com.example.quickcart.ui.main.List;
import com.example.quickcart.ui.main.ListModel;
import com.example.quickcart.ui.main.RecyclerViewAdapterList;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickcart.ui.main.SectionsPagerAdapter;
import com.example.quickcart.databinding.ActivityHomepageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity {
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private ActivityHomepageBinding binding;
    private String p_Name;
    private String p_Image;
    private Long p_ID;
    private String p_details;
    private float p_unitPrice;
    TextView title_homepage;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(Homepage.this, Login.class);
            startActivity(intent);
        }

        ViewPager viewPager = binding.viewPager;
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        // ADAPTER
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        sectionsPagerAdapter.addFragment(new List(),"LIST");
        sectionsPagerAdapter.addFragment(new Cart(),"CART");

        viewPager.setAdapter(sectionsPagerAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(Homepage.this);
                intentIntegrator.setPrompt("For Flash use volume up key");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);

                // set capture activity
                intentIntegrator.setCaptureActivity(Capture.class);

                // Intitialize scan
                intentIntegrator.initiateScan();

            }
        });
    }


    public void setHomepageTitle(String title) {
        title_homepage.setText(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        // Check condition
        if (intentResult.getContents() != null) {
            openDialogue(intentResult.getContents());
        }

        else {
            // When result is null
            // Display Toast
            Toast.makeText(getApplicationContext(), "Nothing scanned", Toast.LENGTH_SHORT).show();
        }

    }

    private void openDialogue(String contents) {
        Dialog dialog = new Dialog(Homepage.this);
        dialog.setContentView(R.layout.scannedproduct_dialogue);

        Button btn_addToCart;
        TextView tv_productName, tv_unitPrice, tv_details;
        ImageView iv_productImage;

        tv_productName = dialog.findViewById(R.id.tv_productName);
        btn_addToCart = dialog.findViewById(R.id.btn_addToCart);
        tv_unitPrice = dialog.findViewById(R.id.tv_unitPrice);
        tv_details = dialog.findViewById(R.id.tv_details);
        iv_productImage =dialog.findViewById(R.id.iv_productImage);


        DatabaseReference myRef = database.getReference("Products").child(contents);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product scannedProduct = dataSnapshot.getValue(Product.class);// ******** from Users class to User
                p_Name = scannedProduct.getProductName();
                p_ID = scannedProduct.getProductID();
                p_Image= scannedProduct.getProductImage();
                p_unitPrice = scannedProduct.getUnitPrice();
                p_details = scannedProduct.getProductDetails();

                Log.d(TAG, "Value is: " + scannedProduct.toString());
                tv_productName.setText(p_Name);
                tv_details.setText(p_details);
                tv_unitPrice.setText(String.valueOf(p_unitPrice));
                //Picasso.get().load(p_Image).into(iv_productImage);
                Glide.with(Homepage.this).load(p_Image).into(iv_productImage);




            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }



        });
        dialog.show();

        btn_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cartRef = database.getReference("Carts").child(currentUserID).child("Items").child(contents);
                cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() == null){
                            int p_quantity = 1;
                            float p_subtotal= p_quantity * p_unitPrice;
                            CartModel addToCart = new CartModel(p_Name, p_ID, p_Image, p_quantity, p_unitPrice, p_subtotal);
                            cartRef.setValue(addToCart);
                            DatabaseReference listRef = database.getReference("Lists").child(currentUserID).child("Items").child(contents);
                            listRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue() == null){
                                        Toast.makeText(getApplicationContext(), "Not in shopping list", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        listRef.child("checkedStatus").setValue(true);
                                        Toast.makeText(getApplicationContext(), "Found in shopping list", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(Homepage.this, "Updating Quantity", Toast.LENGTH_SHORT).show();
                            CartModel cartItem = snapshot.getValue(CartModel.class);
                            int p_quantity = (cartItem.getQuantityPicked())+1;
                            float p_subtotal= p_quantity * p_unitPrice;
                            CartModel addToCart = new CartModel(p_Name, p_ID, p_Image, p_quantity, p_unitPrice, p_subtotal);
                            cartRef.setValue(addToCart);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });



    }

}