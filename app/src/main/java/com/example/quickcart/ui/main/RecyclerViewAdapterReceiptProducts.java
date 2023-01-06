package com.example.quickcart.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcart.R;

import java.util.ArrayList;

public class RecyclerViewAdapterReceiptProducts extends RecyclerView.Adapter <RecyclerViewAdapterReceiptProducts.MyViewHolder> {
    ArrayList<CartModel> productlist;
    Context context;

    public RecyclerViewAdapterReceiptProducts(ArrayList productlist, Context context){
        this.productlist = productlist;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterReceiptProducts.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_item, parent, false); /// NEEDS CANGING
        // Create an instance of MyViewHolder and we pass the view object created above
        RecyclerViewAdapterReceiptProducts.MyViewHolder holder = new RecyclerViewAdapterReceiptProducts.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterReceiptProducts.MyViewHolder holder, int position) {
        holder.tv_name.setText(productlist.get(position).getProductName());
        String quant = String.valueOf(productlist.get(position).getQuantityPicked());
        holder.tv_quant.setText("Qty: "+quant);
        String amount = String.valueOf(productlist.get(position).getSubtotal());
        holder.tv_total.setText("Ksh "+amount );

    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name, tv_quant, tv_total;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_itemNameR);
            tv_quant = itemView.findViewById(R.id.tv_itemQuantR);
            tv_total = itemView.findViewById(R.id.tv_itemTotalR);



        }
    }
}
