package com.example.moonwaygravitystaff;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.example.moonwaygravitystaff.Adapter.illegalBlockAdapter;
import com.example.moonwaygravitystaff.Model.Blocks;
import com.example.moonwaygravitystaff.Model.Comps;
import com.example.moonwaygravitystaff.Model.EntryRecords;
import com.example.moonwaygravitystaff.Model.Floors;
import com.example.moonwaygravitystaff.Model.ParkingSlot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ImproperFragment extends Fragment {

    DatabaseReference blockRef, floorRef, compRef, slotRef;
    TableRow tr;
    TextView slotName, slotLocation, lblSlotName, lblSlotLocation, blockName;
    Spinner spinner;
    List<String> blockNameList, flooridList, compsidList, slotidList;
    TableLayout tableLayout;
    String improper = "IMPROPER";


    private ImproperFragment.OnFragmentInteractionListener mListener;

    public ImproperFragment() {
        // Required empty public constructor
    }

    public static ImproperFragment newInstance(String param1, String param2) {
        ImproperFragment fragment = new ImproperFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void addTableHeader() {
        tableLayout.removeAllViews();
        lblSlotName = new TextView(getContext());
        lblSlotLocation = new TextView(getContext());
        lblSlotName.setText("Slot Name");
        lblSlotLocation.setText("Location");
        lblSlotName.setTextSize(18);
        lblSlotName.setGravity(Gravity.CENTER);
        lblSlotLocation.setTextSize(18);
        lblSlotLocation.setGravity(Gravity.CENTER);
        lblSlotName.setPadding(80, 10, 80, 10);
        lblSlotLocation.setPadding(60, 10, 60, 10);
        lblSlotName.setTextColor(Color.WHITE);
        lblSlotLocation.setTextColor(Color.WHITE);
        tr = new TableRow(getContext());
        tr.addView(lblSlotName);
        tr.addView(lblSlotLocation);
        tr.setBackgroundColor(Color.BLACK);
        tableLayout.addView(tr);
    }

    public void displayAllRecords(String slot, String blocks, String floors) {
        slotName = new TextView(getContext());
        slotLocation = new TextView(getContext());
        slotName.setText(slot);
        slotLocation.setText("Block " + blocks + "    " + floors.toUpperCase());
        slotName.setTextSize(15);
        slotName.setGravity(Gravity.CENTER);
        slotLocation.setTextSize(15);
        slotLocation.setGravity(Gravity.CENTER);
        slotName.setPadding(80, 0, 80, 0);
        slotLocation.setPadding(20, 0, 20, 0);
        tr = new TableRow(getContext());
        tr.addView(slotName);
        tr.addView(slotLocation);
        tr.setBackgroundColor(Color.rgb(220, 220, 220));
        tableLayout.addView(tr);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_improper, container, false);
        tableLayout = view.findViewById(R.id.table_layout);
        spinner = view.findViewById(R.id.dropdown);
        blockName = view.findViewById(R.id.block_name);

        blockNameList = new ArrayList<String>();
        flooridList = new ArrayList<String>();
        blockNameList.add("All");

        blockRef = FirebaseDatabase.getInstance().getReference("Blocks");
        floorRef = FirebaseDatabase.getInstance().getReference("Floors");
        compRef = FirebaseDatabase.getInstance().getReference("Comps");
        slotRef = FirebaseDatabase.getInstance().getReference("ParkingSlot");

        blockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Blocks blocks = data.getValue(Blocks.class);
                    if (blocks.getParkingLotid().equals("-LtmhqyC5ACsE-k8dB8Q")) {
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String block_name = spinner.getSelectedItem().toString();

                if (block_name.equals("All")) {
                    tableLayout.removeAllViews();
                    addTableHeader();

                    blockRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Blocks blocks = data.getValue(Blocks.class);
                                if (blocks.getParkingLotid().equals("-LtmhqyC5ACsE-k8dB8Q")) {
                                    String blockid = data.getKey();
                                    String blockname = blocks.getBlockName();
                                    floorRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                                                Floors floors = data1.getValue(Floors.class);
                                                if (floors.getBlockid().equals(blockid)) {
                                                    String floorid = data1.getKey();
                                                    String floorname = floors.getFloorName();
                                                    compRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot data2 : dataSnapshot.getChildren()) {
                                                                Comps comps = data2.getValue(Comps.class);
                                                                if (comps.getFloorid().equals(floorid)) {
                                                                    String compid = data2.getKey();
                                                                    slotRef.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            for (DataSnapshot data3 : dataSnapshot.getChildren()) {
                                                                                ParkingSlot parkingSlot = data3.getValue(ParkingSlot.class);
                                                                                if (parkingSlot.getStatus() != null) {
                                                                                    if (parkingSlot.getCompid().equals(compid) && parkingSlot.getStatus().toUpperCase().equals(improper)) {
                                                                                        String slotName = parkingSlot.getName();
                                                                                        displayAllRecords(slotName, blockname, floorname);
                                                                                    }
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

                } else {
                    tableLayout.removeAllViews();
                    addTableHeader();
                    blockName.setText("Block " + block_name);

                    blockRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Blocks blocks = data.getValue(Blocks.class);
                                if (blocks.getParkingLotid().equals("-LtmhqyC5ACsE-k8dB8Q") &&  blocks.getBlockName().equals(block_name)) {
                                    String blockid = data.getKey();
                                    String blockname = blocks.getBlockName();
                                    floorRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                                                Floors floors = data1.getValue(Floors.class);
                                                if (floors.getBlockid().equals(blockid)) {
                                                    String floorid = data1.getKey();
                                                    String floorname = floors.getFloorName();
                                                    compRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot data2 : dataSnapshot.getChildren()) {
                                                                Comps comps = data2.getValue(Comps.class);
                                                                if (comps.getFloorid().equals(floorid)) {
                                                                    String compid = data2.getKey();
                                                                    slotRef.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            for (DataSnapshot data3 : dataSnapshot.getChildren()) {
                                                                                ParkingSlot parkingSlot = data3.getValue(ParkingSlot.class);
                                                                                if (parkingSlot.getStatus() != null) {
                                                                                    if (parkingSlot.getCompid().equals(compid) && parkingSlot.getStatus().toUpperCase().equals(improper)) {
                                                                                        String slotName = parkingSlot.getName();
                                                                                        displayAllRecords(slotName, blockname, floorname);
                                                                                    }
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
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
