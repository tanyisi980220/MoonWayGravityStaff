package com.example.moonwaygravitystaff.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moonwaygravitystaff.Model.EntryRecords;
import com.example.moonwaygravitystaff.Model.Transaction;
import com.example.moonwaygravitystaff.R;
import com.example.moonwaygravitystaff.entryRecordDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.entryViewHolder> {
    private Context context;
    private List<Transaction> transactions;
    StorageReference storageRef;
    DatabaseReference custRef,entryRecords;


    public TransactionAdapter(Context context, List<Transaction> transactions){
        this.transactions = transactions;
        this.context = context;
    }

    private Context mContext;

    @NonNull
    @Override
    public TransactionAdapter.entryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_layout,parent,false);
        return new TransactionAdapter.entryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.entryViewHolder holder, int position) {
        Transaction ent = transactions.get(position);

        holder.transactionDate.setText(ent.getTransactionDate());
        holder.transactiontime.setText(ent.getTransactionTime());
        holder.txtType.setText(ent.getTransactionType());


        if(ent.getTransactionType().equals("Reload Credit") && ent.getCustomerId()!=null){
             custRef = FirebaseDatabase.getInstance().getReference("Customer");
             custRef.child(ent.getCustomerId()).addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     holder.customerName.setText(dataSnapshot.child("name").getValue().toString());
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
        }else if(ent.getTransactionType().equals("Pay Parking Fees")){
            entryRecords = FirebaseDatabase.getInstance().getReference("EntryRecords");
            entryRecords.orderByChild("transID").equalTo(ent.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.wtf("hi123",ent.getKey());
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        String licensePlate = data.child("vehicleLicensePlateNumber").getValue().toString();
                        holder.customerName.setText(licensePlate);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }



    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class entryViewHolder extends RecyclerView.ViewHolder {
        public TextView customerName,transactionDate,transactiontime,txtType;
        public Button payParking;
        View mView;

        public entryViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            this.customerName = itemView.findViewById(R.id.txtCustomerName);
            this.transactionDate = itemView.findViewById(R.id.txtDate);
            this.transactiontime = itemView.findViewById(R.id.txtTime);
            this.txtType = itemView.findViewById(R.id.txtTransactionType);


        }


//        public void onClick(View view) {
////            onItemClickListener.onItemClick(getLayoutPosition());
////            EntryRecords er = entryRecords.get(getLayoutPosition());
////            Intent intent = new Intent(context, entryRecordDetail.class);
////            intent.putExtra("vehicleLicensePlate", er.getVehicleLicensePlateNumber());
////            mContext.startActivity(intent);
//            if (onItemClickListener != null) {
//                onItemClickListener.onItemClick(getLayoutPosition());
//            }
//        }
    }


}
