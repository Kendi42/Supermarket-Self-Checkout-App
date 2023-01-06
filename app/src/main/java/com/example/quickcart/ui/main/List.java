package com.example.quickcart.ui.main;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickcart.Account;
import com.example.quickcart.Homepage;
import com.example.quickcart.Login;
import com.example.quickcart.Product;
import com.example.quickcart.R;
import com.example.quickcart.Receipt;
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


public class List extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    ArrayList<ListModel> shoppingItems = new ArrayList<>();
    ArrayList<Product> productItems = new ArrayList<>();
    Button btn_add;
    String currentUserID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        }

        RecyclerView recyclerViewcart = view.findViewById(R.id.recyclerViewList);
        btn_add= view.findViewById(R.id.btn_addToList);
        TextView tv_estimate = view.findViewById(R.id.tv_estimate);

        DatabaseReference estimateRef = database.getReference("Lists").child(currentUserID).child("EstimatedTotal");
        estimateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estimation = "Ksh "+ String.valueOf(snapshot.getValue());
                tv_estimate.setText(estimation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TextView sidemenu = view.findViewById(R.id.sidemenuList);
        NavigationView nv_sidemenu = view.findViewById(R.id.nv_sidemenu2);
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
                                Toast.makeText(getContext(), "No product found", Toast.LENGTH_SHORT).show();
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

                    SearchView searchViewproducts = dialog.findViewById(R.id.sv_productList);
                    dialog.show();


                }

                return false;
            }
        });
        DatabaseReference idref = database.getReference("Lists").child(currentUserID).child("Items");
        idref.addValueEventListener(new ValueEventListener() {
            int i =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shoppingItems.clear();
                float total = 0;
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    ListModel item = itemSnapshot.getValue(ListModel.class);
                    Log.d(TAG, "Value is: " + item.toString());
                    shoppingItems.add(item);
                    recyclerViewcart.setAdapter(new RecyclerViewAdapterList(shoppingItems, getContext(), currentUserID));
                    recyclerViewcart.setLayoutManager(new LinearLayoutManager(getContext()));
                    total = total +item.getSubtotal();
                    estimateRef.setValue(total);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.productlist_dialog);
                SearchView searchView = dialog.findViewById(R.id.sv_productList);
                RecyclerView recyclerViewproducts = dialog.findViewById(R.id.rv_productList);
                RecyclerViewAdapterDialog dialogAdapter = new RecyclerViewAdapterDialog(productItems, getContext(), currentUserID);

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
                            Toast.makeText(getContext(), "No product found", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            dialogAdapter.setFilteredList(filteredList);

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
                        recyclerViewproducts.setAdapter(dialogAdapter);


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Product item = snapshot.getValue(Product.class);
                        Log.d(TAG, "chval is: " +item.toString());
                        productItems.add(item);
                        recyclerViewproducts.setAdapter(dialogAdapter);

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Product item = snapshot.getValue(Product.class);
                        Log.d(TAG, "chval is: " +item.toString());
                        productItems.add(item);
                        recyclerViewproducts.setAdapter(dialogAdapter);

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Product item = snapshot.getValue(Product.class);
                        Log.d(TAG, "chval is: " +item.toString());
                        productItems.add(item);
                        recyclerViewproducts.setAdapter(dialogAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                recyclerViewproducts.setLayoutManager(new LinearLayoutManager(getContext()));

                SearchView searchViewproducts = dialog.findViewById(R.id.sv_productList);

                dialog.show();

            }
        });


        return view;

    }


}