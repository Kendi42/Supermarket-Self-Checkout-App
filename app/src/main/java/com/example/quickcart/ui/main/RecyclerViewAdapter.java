package com.example.quickcart.ui.main;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.MyViewHolder> {
    // CLASS VARIABLES WILL BE, your list and the context
    ArrayList<CartModel> cartList;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String currentUserID;


    public RecyclerViewAdapter(ArrayList cartList, Context context, String currentUserID){
        this.cartList = cartList;
        this.context = context;
        this.currentUserID =currentUserID;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        // Create an instance of MyViewHolder and we pass the view object created above
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        int QuantityPicked = cartList.get(position).getQuantityPicked();
        float Subtotal = cartList.get(position).getSubtotal();
        float UnitPrice = cartList.get(position).getUnitPrice();
        // the text
        holder.tv_productNameCart.setText(cartList.get(position).getProductName());
        holder.tv_quantityCart2.setText(String.valueOf(QuantityPicked));
        String subtotal = "Ksh: " + Subtotal;
        holder.tv_subtotalCart2.setText(subtotal);
        Glide.with(this.context).load(cartList.get(position).getImageURL()).into(holder.iv_productImageCart);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartList != null){
                    int i = holder.getAdapterPosition();
                    Long ID = cartList.get(i).getProductID();
                    DatabaseReference itemref = database.getReference("Carts").child(currentUserID).child("Items").child(String.valueOf(ID));
                    Log.d(TAG, "removing: " + itemref );
                    if(cartList.size() == 1){
                        DatabaseReference totalref = database.getReference("Carts").child(currentUserID).child("Total");
                        totalref.setValue(0);
                    }
                    itemref.removeValue();
                    cartList.remove(i);
                    notifyDataSetChanged();
                    notifyItemRemoved(i);
                    Toast.makeText(view.getContext(), "Item Removed" , Toast.LENGTH_SHORT).show();
                }}
        });

        holder.reduceQuant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartList != null){
                    int i = holder.getAdapterPosition();
                    Long ID = cartList.get(i).getProductID();
                    DatabaseReference itemref = database.getReference("Carts").child(currentUserID).child("Items").child(String.valueOf(ID));
                    Log.d(TAG, "reducingquantity for: " + itemref );
                    if(QuantityPicked > 1) {
                        int newVal = (QuantityPicked - 1);
                        float newSubtotal = (newVal * UnitPrice);
                        itemref.child("subtotal").setValue(newSubtotal);
                        itemref.child("quantityPicked").setValue(newVal);
                        notifyDataSetChanged();
                    }
                    else if(QuantityPicked == 1){
                        Toast.makeText(view.getContext(), "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
                    }
                }}
        });
    }
    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // CLASS MY VIEW HOLDER
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_productImageCart;
        TextView tv_subtotalCart2;
        TextView tv_quantityCart2;
        TextView tv_productNameCart, reduceQuant, remove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_productImageCart=itemView.findViewById(R.id.iv_productImageCart);
            tv_subtotalCart2=itemView.findViewById(R.id.tv_subtotalCart2);
            tv_quantityCart2=itemView.findViewById(R.id.tv_quantityCart2);
            tv_productNameCart=itemView.findViewById(R.id.tv_productNameCart);
            reduceQuant=itemView.findViewById(R.id.reduceQuantCart);
            remove=itemView.findViewById(R.id.deletefromCart);
        }
    }
}

