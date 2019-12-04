package com.example.moonwaygravitystaff.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moonwaygravitystaff.Model.Blocks;
import com.example.moonwaygravitystaff.Model.Floors;
import com.example.moonwaygravitystaff.R;

import java.util.List;

public class floorAdapter extends RecyclerView.Adapter<floorAdapter.floorsViewHolder>{
    private Context context;
    private List<Floors> floorsRecord;

    public floorAdapter(Context context, List<Floors> floorsRecord){
        this.floorsRecord = floorsRecord;
        this.context = context;
    }

    @NonNull
    @Override
    public floorAdapter.floorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.floor_row,parent,false);
        return new floorAdapter.floorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull floorAdapter.floorsViewHolder holder, int position) {
        Floors floors = floorsRecord.get(position);
        holder.floorsName.setText(floors.getFloorName().toUpperCase());
        int unparked = floors.getFlowNo() - floors.getParkedNo();
        holder.flowNo.setText(String.valueOf(unparked));
        holder.parkedNo.setText(String.valueOf(floors.getParkedNo()));
    }

    @Override
    public int getItemCount() {
        return floorsRecord.size();
    }

    public class floorsViewHolder extends RecyclerView.ViewHolder{
        public TextView floorsName, flowNo, parkedNo;
        View mView;

        public floorsViewHolder(View itemView){
            super(itemView);
            floorsName = itemView.findViewById(R.id.floor_name);
            flowNo = itemView.findViewById(R.id.unparked);
            parkedNo = itemView.findViewById(R.id.parked_no);
        }
    }


}

