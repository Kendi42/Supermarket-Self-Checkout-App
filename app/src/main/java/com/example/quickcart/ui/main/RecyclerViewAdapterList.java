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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcart.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerViewAdapterList extends RecyclerView.Adapter <RecyclerViewAdapterList.MyViewHolder> {
    ArrayList<ListModel> shoppingList;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String currentUserID;


    public RecyclerViewAdapterList(ArrayList shoppingList, Context context, String currentUserID){
        this.shoppingList = shoppingList;
        this.context = context;
        this.currentUserID = currentUserID;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item, parent, false);
        // Create an instance of MyViewHolder and we pass the view object created above
        RecyclerViewAdapterList.MyViewHolder holder = new RecyclerViewAdapterList.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterList.MyViewHolder holder, int position) {
        int QuantityPicked = shoppingList.get(position).getQuantityPicked();
        float Subtotal = shoppingList.get(position).getSubtotal();
        float UnitPrice = shoppingList.get(position).getUnitPrice();
        String tesxtsubtotal = "Ksh: " + String.valueOf(Subtotal);
        holder.quantityList.setText(String.valueOf(QuantityPicked));
        holder.subtotalEstm.setText(tesxtsubtotal);
        Long ID = shoppingList.get(position).getProductID();
        DatabaseReference listref = database.getReference("Lists").child(currentUserID).child("Items").child(String.valueOf(ID)).child("checkedStatus");
        holder.productName.setText(shoppingList.get(position).getProductName());
        Boolean checkedStatus= shoppingList.get(position).getCheckedStatus();
        if(checkedStatus){
            holder.productName.setChecked(true);
            holder.productName.setPaintFlags(holder.productName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.productName.setChecked(false);
            holder.productName.setPaintFlags(holder.productName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }
        holder.productName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    holder.productName.setPaintFlags(holder.productName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    listref.setValue(true);

                }
                else {
                    holder.productName.setPaintFlags(holder.productName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    listref.setValue(false);
                }

            }
        });




        holder.removeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shoppingList != null){
                int i = holder.getAdapterPosition();
                Long ID = shoppingList.get(i).getProductID();
                DatabaseReference itemref = database.getReference("Lists").child(currentUserID).child("Items").child(String.valueOf(ID));
                Log.d(TAG, "removing: " + itemref );
                if(shoppingList.size() == 1){
                    DatabaseReference totalref = database.getReference("Lists").child(currentUserID).child("EstimatedTotal");
                    totalref.setValue(0);
                }
                itemref.removeValue();
                shoppingList.remove(i);
                notifyDataSetChanged();
                notifyItemRemoved(i);
                Toast.makeText(view.getContext(), "Item Removed" , Toast.LENGTH_SHORT).show();

            }

            }
        });

        holder.addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shoppingList != null){
                int i = holder.getAdapterPosition();
                Long ID = shoppingList.get(i).getProductID();
                DatabaseReference itemref = database.getReference("Lists").child(currentUserID).child("Items").child(String.valueOf(ID));
                Log.d(TAG, "addingquantity for: " + itemref );
                int newVal = (QuantityPicked+1);
                float newSubtotal = (newVal * UnitPrice);
                itemref.child("subtotal").setValue(newSubtotal);
                itemref.child("quantityPicked").setValue(newVal);
                notifyDataSetChanged();
            }}
        });

        holder.reduceQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shoppingList != null){
                int i = holder.getAdapterPosition();
                Long ID = shoppingList.get(i).getProductID();
                DatabaseReference itemref = database.getReference("Lists").child(currentUserID).child("Items").child(String.valueOf(ID));
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
        return shoppingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox productName;
        TextView reduceQ,addQ, removeList, quantityList, subtotalEstm;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.checkBoxList);
            reduceQ = itemView.findViewById(R.id.reduceQuantity);
            addQ = itemView.findViewById(R.id.addquantity);
            removeList = itemView.findViewById(R.id.removeList);
            quantityList = itemView.findViewById(R.id.tv_quantityList);
            subtotalEstm = itemView.findViewById(R.id.tv_estimateSubtotal);



        }
    }
}
