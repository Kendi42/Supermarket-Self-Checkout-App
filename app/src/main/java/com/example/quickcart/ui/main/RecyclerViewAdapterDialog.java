package com.example.quickcart.ui.main;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickcart.Product;
import com.example.quickcart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerViewAdapterDialog extends RecyclerView.Adapter <RecyclerViewAdapterDialog.MyViewHolder> {
    ArrayList<Product> productList;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Product item;
    String currentUserID;

    public void setFilteredList(ArrayList<Product> filteredList){
        this.productList = filteredList;
        notifyDataSetChanged();

    }


    public RecyclerViewAdapterDialog(ArrayList productList, Context context, String currentUserID){
        this.productList = productList;
        this.context = context;
        this.currentUserID = currentUserID;

    }

    @NonNull
    @Override
    public RecyclerViewAdapterDialog.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productitem, parent, false);
        // Create an instance of MyViewHolder and we pass the view object created above
        RecyclerViewAdapterDialog.MyViewHolder holder = new RecyclerViewAdapterDialog.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterDialog.MyViewHolder holder, int position) {
        holder.tv_productNameProducts.setText(productList.get(position).getProductName());
        Glide.with(this.context).load(productList.get(position).getProductImage()).into(holder.iv_productImageProducts);
        holder.btn_addToShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = holder.getAdapterPosition();
                Long ID = productList.get(i).getProductID();
                DatabaseReference listref = database.getReference("Lists").child(currentUserID).child("Items").child(String.valueOf(ID));
                DatabaseReference productref = database.getReference("Products").child(String.valueOf(ID));
                productref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        item = snapshot.getValue(Product.class);
                        String productName = item.getProductName();
                        Long productID = item.getProductID();
                        float unitPrice = item.getUnitPrice();
                        int quantityPicked = 1;
                        float subtotal = unitPrice * quantityPicked;
                        Boolean checkedStatus = false;
                        ListModel listItem = new ListModel(productName, quantityPicked, productID, unitPrice,  subtotal, checkedStatus);
                        listref.setValue(listItem);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Added Value is: " + item.toString());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_productImageProducts;
        TextView tv_productNameProducts;
        Button btn_addToShopping;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_productImageProducts = itemView.findViewById(R.id.iv_productImageProducts);
            tv_productNameProducts = itemView.findViewById(R.id.tv_productNameProducts);
            btn_addToShopping = itemView.findViewById(R.id.btn_addToShopping);


        }
    }
}
