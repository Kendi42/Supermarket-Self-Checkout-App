package com.example.quickcart.ui.main;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcart.Homepage;
import com.example.quickcart.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerViewAdapterReceipt extends RecyclerView.Adapter <RecyclerViewAdapterReceipt.MyViewHolder> {
    ArrayList<ReceiptModel> receiptList;
    ArrayList<CartModel> receiptitemList = new ArrayList<>();;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String currentUserID;


    public RecyclerViewAdapterReceipt(ArrayList receiptList, Context context, String currentUserID ){
        this.receiptList = receiptList;
        this.context = context;
        this.currentUserID = currentUserID;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterReceipt.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiptdate, parent, false); /// NEEDS CANGING
        // Create an instance of MyViewHolder and we pass the view object created above
        RecyclerViewAdapterReceipt.MyViewHolder holder = new RecyclerViewAdapterReceipt.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterReceipt.MyViewHolder holder, int position) {
        holder.tv_receiptDate.setText(receiptList.get(position).getDate());
        String amount= receiptList.get(position).getTotal();
        holder.tv_total.setText("Ksh "+ amount);
        String date = receiptList.get(position).getDate();
        DatabaseReference receiptRef = database.getReference("Receipts").child(currentUserID).child(date).child("Items");
        receiptRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiptitemList.clear();
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    CartModel item = itemSnapshot.getValue(CartModel.class);
                    receiptitemList.add(item);
                    holder.rv_receipt.setAdapter(new RecyclerViewAdapterReceiptProducts(receiptitemList, context));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.rv_receipt.setLayoutManager(new LinearLayoutManager(context));


    }

    @Override
    public int getItemCount() {
        return receiptList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_receiptDate, tv_total;
        RecyclerView rv_receipt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_receiptDate = itemView.findViewById(R.id.tv_receiptDate);
            tv_total = itemView.findViewById(R.id.tv_totalREceipt);
            rv_receipt = itemView.findViewById(R.id.rv_ReceiptCard);





        }
    }
}
