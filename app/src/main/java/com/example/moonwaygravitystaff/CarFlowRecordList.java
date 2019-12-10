package com.example.moonwaygravitystaff;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.moonwaygravitystaff.Adapter.floorAdapter;
import com.example.moonwaygravitystaff.Model.Blocks;
import com.example.moonwaygravitystaff.Model.Floors;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CarFlowRecordList extends AppCompatActivity {
    DatabaseReference floorRef, blockRef;
    List<Floors> floorsList;
    floorAdapter fAdapter;
    private RecyclerView floorRecycle;
    String blockid;
    TextView count_floor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_flow_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        floorsList = new ArrayList<>();
        floorRecycle = findViewById(R.id.floorRecycler);
        count_floor = findViewById(R.id.count_floor);

        floorRecycle.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        floorRecycle.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();

        blockRef = FirebaseDatabase.getInstance().getReference("Blocks");
        blockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Blocks blocks = data.getValue(Blocks.class);
                    if(blocks.getBlockName().equals(intent.getStringExtra("block_name"))) {
                        blockid = data.getKey().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        floorRef = FirebaseDatabase.getInstance().getReference("Floors");

        floorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Floors floors = data.getValue(Floors.class);
                    if(floors.getBlockid().equals(blockid)) {
                        floorsList.add(floors);
                    }
                }

                fAdapter = new floorAdapter(getBaseContext(), floorsList);
                count_floor.setText(String.valueOf(fAdapter.getItemCount()) + " floor(s) found in block " + intent.getStringExtra("block_name"));
                floorRecycle.setAdapter(fAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

