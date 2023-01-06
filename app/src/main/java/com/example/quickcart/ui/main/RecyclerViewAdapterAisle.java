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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickcart.Product;
import com.example.quickcart.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerViewAdapterAisle extends RecyclerView.Adapter <RecyclerViewAdapterAisle.MyViewHolder> {
    ArrayList<Product> productList;
    Context context;

    public void setFilteredList(ArrayList<Product> filteredList){
        this.productList = filteredList;
        notifyDataSetChanged();

    }

    public RecyclerViewAdapterAisle(ArrayList productList, Context context){
        this.productList = productList;
             this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterAisle.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aisle_item, parent, false);
        // Create an instance of MyViewHolder and we pass the view object created above
        RecyclerViewAdapterAisle.MyViewHolder holder = new RecyclerViewAdapterAisle.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterAisle.MyViewHolder holder, int position) {
        holder.tv_productNameProducts.setText(productList.get(position).getProductName());
        holder.tv_aisleinfo.setText(productList.get(position).getAisle());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_productNameProducts, tv_aisleinfo;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_aisleinfo = itemView.findViewById(R.id.tv_aisleinfo);
            tv_productNameProducts = itemView.findViewById(R.id.tv_productNameProducts);


        }
    }
}
