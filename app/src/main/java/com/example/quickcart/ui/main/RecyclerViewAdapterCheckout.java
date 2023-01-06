package com.example.quickcart.ui.main;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickcart.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerViewAdapterCheckout extends RecyclerView.Adapter <RecyclerViewAdapterCheckout.MyViewHolder> {
    ArrayList<CartModel> checkoutList;
    Context context;

    public RecyclerViewAdapterCheckout(ArrayList checkoutList, Context context){
        this.checkoutList = checkoutList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterCheckout.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item, parent, false); /// NEEDS CANGING
        // Create an instance of MyViewHolder and we pass the view object created above
        RecyclerViewAdapterCheckout.MyViewHolder holder = new RecyclerViewAdapterCheckout.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterCheckout.MyViewHolder holder, int position) {
        int QuantityPicked = checkoutList.get(position).getQuantityPicked();
        float Subtotal = checkoutList.get(position).getSubtotal();
        float UnitPrice = checkoutList.get(position).getUnitPrice();
        // the text
        String subtotal = "Ksh: " + Subtotal;
        holder.productName.setText(checkoutList.get(position).getProductName());
        holder.productQuantity.setText(String.valueOf(QuantityPicked));
        holder.productSubtotal.setText(subtotal);
        Glide.with(this.context).load(checkoutList.get(position).getImageURL()).into(holder.productImage);




    }

    @Override
    public int getItemCount() {
        return checkoutList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView productName;
        TextView productSubtotal;
        TextView productQuantity;
        ImageView productImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tv_productNameCheck);
            productSubtotal = itemView.findViewById(R.id.tv_subtotalCheck2);
            productQuantity = itemView.findViewById(R.id.tv_quantityCheck2);
            productImage = itemView.findViewById(R.id.iv_productImageCheck);


        }
    }
}
