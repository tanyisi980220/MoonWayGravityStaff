package com.example.moonwaygravitystaff;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moonwaygravitystaff.Adapter.blockAdapter;
import com.example.moonwaygravitystaff.Model.Blocks;
import com.example.moonwaygravitystaff.Model.Floors;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_spinner_item;

public class CarFlowFragment extends Fragment {
    DatabaseReference blockRef, floorRef;
    TableRow tr;
    TextView floorName, parkedNo, flowNo, lblfloorName, lblparkedNo, lblflowNo, blockName;
    Spinner spinner;
    List<String> blockNameList, blockidList;
    TableLayout tableLayout;

    private OnFragmentInteractionListener mListener;

    public CarFlowFragment() {
        // Required empty public constructor
    }

    public void addTableHeader() {
        tableLayout.removeAllViews();
        lblfloorName = new TextView(getContext());
        lblparkedNo = new TextView(getContext());
        lblflowNo = new TextView(getContext());
        lblfloorName.setText("Floor");
        lblflowNo.setText("Moving Number");
        lblparkedNo.setText("Parked Number");
        lblfloorName.setTextSize(18);
        lblfloorName.setGravity(Gravity.CENTER);
        lblflowNo.setTextSize(18);
        lblflowNo.setGravity(Gravity.CENTER);
        lblparkedNo.setTextSize(18);
        lblparkedNo.setGravity(Gravity.CENTER);
        lblfloorName.setPadding(10, 10, 10, 10);
        lblparkedNo.setPadding(10, 10, 10, 10);
        lblflowNo.setPadding(10, 10, 10, 10);
        lblfloorName.setTextColor(Color.WHITE);
        lblflowNo.setTextColor(Color.WHITE);
        lblparkedNo.setTextColor(Color.WHITE);
        tr = new TableRow(getContext());
        tr.addView(lblfloorName);
        tr.addView(lblflowNo);
        tr.addView(lblparkedNo);
        tr.setBackgroundColor(Color.BLACK);
        tableLayout.addView(tr);
    }

    public static CarFlowFragment newInstance(String param1, String param2) {
        CarFlowFragment fragment = new CarFlowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_flow, container, false);
        tableLayout = view.findViewById(R.id.table_layout);
        spinner = view.findViewById(R.id.dropdown);
        blockName = view.findViewById(R.id.block_name);

        blockNameList = new ArrayList<String>();
        blockidList = new ArrayList<String>();
        blockNameList.add("All");

        blockRef = FirebaseDatabase.getInstance().getReference("Blocks");
        floorRef = FirebaseDatabase.getInstance().getReference("Floors");

        blockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Blocks blocks = data.getValue(Blocks.class);
                    if (blocks.getParkingLotid().equals("-LtmhqyC5ACsE-k8dB8Q")) {

                        final String blockid = data.getKey();
                        blockidList.add(data.getKey());
                        blockNameList.add(data.child("blockName").getValue().toString());


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, blockNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String block_name = spinner.getSelectedItem().toString();
//                Toast.makeText(view.getContext(), block_name, Toast.LENGTH_SHORT).show();
                if (block_name.equals("All")) {
                    Toast.makeText(getContext(), String.valueOf(tableLayout.getChildCount()), Toast.LENGTH_SHORT).show();
                    tableLayout.removeAllViews();
                    addTableHeader();
                    blockName.setText("All Blocks");
                    floorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot floor : dataSnapshot.getChildren()) {

                                Floors floors = floor.getValue(Floors.class);
                                for (int i = 0; i < blockidList.size(); i++) {
                                    if (blockidList.get(i).equals(floors.getBlockid())) {
                                        floorName = new TextView(getContext());
                                        parkedNo = new TextView(getContext());
                                        flowNo = new TextView(getContext());
                                        floorName.setText(floors.getFloorName().toUpperCase());
                                        flowNo.setText(String.valueOf(floors.getFlowNo()));
                                        parkedNo.setText(String.valueOf(floors.getParkedNo()));
                                        floorName.setTextSize(20);
                                        floorName.setGravity(Gravity.CENTER);
                                        flowNo.setTextSize(20);
                                        flowNo.setGravity(Gravity.CENTER);
                                        parkedNo.setTextSize(20);
                                        parkedNo.setGravity(Gravity.CENTER);
                                        tr = new TableRow(getContext());
                                        tr.addView(floorName);
                                        tr.addView(flowNo);
                                        tr.addView(parkedNo);
                                        tr.setBackgroundColor(Color.rgb(220, 220, 220));
                                        tableLayout.addView(tr);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    // Toast.makeText(getContext(), String.valueOf(tableLayout.getChildCount()), Toast.LENGTH_SHORT).show();
                } else {
                    addTableHeader();
                    blockName.setText("Block " + block_name);
                    blockRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Blocks blocks = data.getValue(Blocks.class);

                                if (blocks.getParkingLotid().equals("-LtmhqyC5ACsE-k8dB8Q") && block_name.equals(data.child("blockName").getValue())) {
                                    String bid = data.getKey();
                                    floorRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot floor : dataSnapshot.getChildren()) {
                                                Floors floors = floor.getValue(Floors.class);

                                                if (bid.equals(floors.getBlockid())) {
                                                    floorName = new TextView(getContext());
                                                    parkedNo = new TextView(getContext());
                                                    flowNo = new TextView(getContext());
                                                    floorName.setText(floors.getFloorName().toUpperCase());
                                                    flowNo.setText(String.valueOf(floors.getFlowNo()));
                                                    parkedNo.setText(String.valueOf(floors.getParkedNo()));
                                                    floorName.setTextSize(20);
                                                    floorName.setGravity(Gravity.CENTER);
                                                    flowNo.setTextSize(20);
                                                    flowNo.setGravity(Gravity.CENTER);
                                                    parkedNo.setTextSize(20);
                                                    parkedNo.setGravity(Gravity.CENTER);
                                                    tr = new TableRow(getContext());
                                                    tr.addView(floorName);
                                                    tr.addView(flowNo);
                                                    tr.addView(parkedNo);
                                                    tr.setBackgroundColor(Color.rgb(220, 220, 220));
                                                    tableLayout.addView(tr);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
