package com.example.moonwaygravitystaff.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.moonwaygravitystaff.Model.EntryRecords;
import com.example.moonwaygravitystaff.R;
import com.example.moonwaygravitystaff.entryRecordDetail;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.net.URL;
import java.util.List;
import java.util.Map;

public class EntryRecordListAdapter extends RecyclerView.Adapter<EntryRecordListAdapter.entryViewHolder> {
    private Context context;
    private List<EntryRecords> entryRecords;
    StorageReference storageRef;


    public onBindCallBack onBind;

    private Context mContext;
    private OnItemClickListener onItemClickListener;


    public EntryRecordListAdapter(Context context, List<EntryRecords> entryRecords){
        this.entryRecords = entryRecords;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public EntryRecordListAdapter.entryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.entry_record_list_layout,parent,false);
        return new EntryRecordListAdapter.entryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryRecordListAdapter.entryViewHolder holder, int position) {
        EntryRecords ent = entryRecords.get(position);
        holder.licensePlate.setText(ent.getVehicleLicensePlateNumber());
        holder.entryDate.setText(ent.getDate());
        holder.entrytime.setText(ent.getTime());
        holder.setPostImage(context,ent.getLicensePlateImage());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, entryRecordDetail.class);
                intent.putExtra("vehicleLicensePlate", ent.getVehicleLicensePlateNumber());
                intent.putExtra("entryDate", ent.getDate());
                intent.putExtra("entryTime", ent.getTime());
                context.startActivity(intent);

            }
        });

        if(onBind!=null){
            onBind.onViewBound(holder,position,ent);
        }

    }



    @Override
    public int getItemCount() {
        return entryRecords.size();
    }

    public class entryViewHolder extends RecyclerView.ViewHolder {
        public TextView licensePlate,entryDate,entrytime;
        public ImageButton image;
        public Button payParking;
        View mView;

        public entryViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            this.licensePlate = itemView.findViewById(R.id.txtLicensePlate);
            this.entryDate = itemView.findViewById(R.id.txtDate);
            this.entrytime = itemView.findViewById(R.id.txtTime);
            this.image = itemView.findViewById(R.id.image);


        }
        public void setPostImage(Context ctx1,String postImage){
            image= mView.findViewById(R.id.image);
            FirebaseStorage storage= FirebaseStorage.getInstance();
            //storageRef = storage.getReferenceFromUrl("gs://parking-management-syste-d2bff.appspot.com/VehicleLicensePlate/"+postImage);
            storageRef = storage.getReference();
            storageRef.child("VehicleLicensePlate/"+postImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(ctx1).load(uri).into(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
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
    public interface onBindCallBack {
        void onViewBound(entryViewHolder viewHolder, int position, EntryRecords ent);
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

}
