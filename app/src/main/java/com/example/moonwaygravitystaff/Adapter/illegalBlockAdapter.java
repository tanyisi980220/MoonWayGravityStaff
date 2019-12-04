package com.example.moonwaygravitystaff.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moonwaygravitystaff.CarFlowRecordList;
import com.example.moonwaygravitystaff.IllegalRecordList;
import com.example.moonwaygravitystaff.Model.Blocks;
import com.example.moonwaygravitystaff.R;

import java.util.List;

public class illegalBlockAdapter extends RecyclerView.Adapter<illegalBlockAdapter.illegalblockViewHolder> {
    private Context context;
    private List<Blocks> blockRecord;

    public illegalBlockAdapter(Context context, List<Blocks> blockRecord) {
        this.blockRecord = blockRecord;
        this.context = context;
    }

    @NonNull
    @Override
    public illegalBlockAdapter.illegalblockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.block_row, parent, false);
        return new illegalBlockAdapter.illegalblockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull illegalBlockAdapter.illegalblockViewHolder holder, int position) {
        Blocks blocks = blockRecord.get(position);
        holder.blockName.setText("Block " + blocks.getBlockName());
        holder.floorNo.setText(String.valueOf(blocks.getFloorNo()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), IllegalRecordList.class);
                intent.putExtra("block_name", blocks.getBlockName());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blockRecord.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class illegalblockViewHolder extends RecyclerView.ViewHolder {
        public TextView blockName, floorNo;
        View mView;

        public illegalblockViewHolder(View itemView) {
            super(itemView);
            blockName = itemView.findViewById(R.id.block_name);
            floorNo = itemView.findViewById(R.id.floor_no);
        }
    }


}