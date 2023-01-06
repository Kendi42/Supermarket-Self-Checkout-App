package com.example.quickcart.ui.main;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickcart.Account;
import com.example.quickcart.Checkout;
import com.example.quickcart.Homepage;
import com.example.quickcart.Login;
import com.example.quickcart.Product;
import com.example.quickcart.R;
import com.example.quickcart.Receipt;
import com.example.quickcart.databinding.ActivityMainBinding;
import com.example.quickcart.databinding.FragmentCartBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Cart extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    ArrayList<CartModel> cartItems = new ArrayList<>();
    ArrayList<Product> productItems = new ArrayList<>();
    String currentUserID;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_cart, container, false);
        RecyclerView recyclerViewcart = view.findViewById(R.id.recyclerView_cart);
        Button btn_checkout = view.findViewById(R.id.btn_checkout);
        TextView tv_total = view.findViewById(R.id.tv_total);
        DatabaseReference totalRef = database.getReference("Carts").child(currentUserID).child("Total");
        totalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String total = "Ksh "+ String.valueOf(snapshot.getValue());
                tv_total.setText(total);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        TextView sidemenu = view.findViewById(R.id.sidemenuCart);
        NavigationView nv_sidemenu = view.findViewById(R.id.nv_sidemenu);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        }

        sidemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int menuVisibility =nv_sidemenu.getVisibility();

                if(menuVisibility == 0){
                    nv_sidemenu.setVisibility(View.INVISIBLE);
                    sidemenu.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menuicon));

                }
                else{
                    nv_sidemenu.setVisibility(View.VISIBLE);
                    sidemenu.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.back));
                }


            }
        });

        nv_sidemenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.toString().equals("Logout")){
                    nv_sidemenu.setVisibility(View.INVISIBLE);
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();

                }
                else if(item.toString().equals("My Receipts")){
                    nv_sidemenu.setVisibility(View.INVISIBLE);
                    sidemenu.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menuicon));
                    Intent intent = new Intent(getContext(), Receipt.class);
                    startActivity(intent);

                }
                else if(item.toString().equals("My Account")){
                    nv_sidemenu.setVisibility(View.INVISIBLE);
                    sidemenu.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menuicon));
                    Intent intent = new Intent(getContext(), Account.class);
                    startActivity(intent);

                }
                else if(item.toString().equals("Find Product Aisle")){
                    nv_sidemenu.setVisibility(View.INVISIBLE);
                    sidemenu.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menuicon));
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.productlist_dialog);
                    SearchView searchView = dialog.findViewById(R.id.sv_productList);
                    RecyclerView recyclerViewproducts = dialog.findViewById(R.id.rv_productList);
                    RecyclerViewAdapterAisle aisleAdapter = new RecyclerViewAdapterAisle(productItems, getContext());
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            ArrayList<Product> filteredList = new ArrayList<>();
                            for(Product product: productItems){
                                if(product.getProductName().toLowerCase().contains(s.toLowerCase())){
                                    filteredList.add(product);

                                }
                            }
                            if(filteredList.isEmpty()){
                                Toast.makeText(getContext(), "No such product found", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                aisleAdapter.setFilteredList(filteredList);

                            }
                            return true;
                        }
                    });


                    DatabaseReference productref = database.getReference("Products");
                    productItems.clear();
                    productref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Product item = snapshot.getValue(Product.class);
                            Log.d(TAG, "chval is: " +item.toString());
                            productItems.add(item);
                            recyclerViewproducts.setAdapter(aisleAdapter);


                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Product item = snapshot.getValue(Product.class);
                            Log.d(TAG, "chval is: " +item.toString());
                            productItems.add(item);
                            recyclerViewproducts.setAdapter(new RecyclerViewAdapterAisle(productItems, getContext()));

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            Product item = snapshot.getValue(Product.class);
                            Log.d(TAG, "chval is: " +item.toString());
                            productItems.add(item);
                            recyclerViewproducts.setAdapter(new RecyclerViewAdapterAisle(productItems, getContext()));

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Product item = snapshot.getValue(Product.class);
                            Log.d(TAG, "chval is: " +item.toString());
                            productItems.add(item);
                            recyclerViewproducts.setAdapter(new RecyclerViewAdapterAisle(productItems, getContext()));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    recyclerViewproducts.setLayoutManager(new LinearLayoutManager(getContext()));
                    dialog.show();

                }

                return false;
            }
        });




        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartItems.isEmpty()){
                    Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getContext(), Checkout.class);
                    startActivity(intent);
                }

            }
        });

        DatabaseReference idref = database.getReference("Carts").child(currentUserID).child("Items");
        idref.addValueEventListener(new ValueEventListener() {
            int i =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                float total = 0;
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    CartModel item = itemSnapshot.getValue(CartModel.class);
                    Log.d(TAG, "Value is: " + item.toString());
                    cartItems.add(item);
                    recyclerViewcart.setAdapter(new RecyclerViewAdapter(cartItems, getContext(), currentUserID));
                    total = total +item.getSubtotal();
                    totalRef.setValue(total);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerViewcart.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;




    }



}