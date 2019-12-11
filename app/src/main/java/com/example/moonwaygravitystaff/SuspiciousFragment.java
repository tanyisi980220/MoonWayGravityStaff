package com.example.moonwaygravitystaff;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.moonwaygravitystaff.Model.Blocks;
import com.example.moonwaygravitystaff.Model.EntryRecords;
import com.example.moonwaygravitystaff.Model.Floors;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SuspiciousFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SuspiciousFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuspiciousFragment extends Fragment {

    DatabaseReference blockRef, floorRef, entryRef, floor1Ref;
    TableRow tr;
    TextView licenseNo, entryDateTime, CarFlowLocation, lbllicenseNo, lblentryDateTime, lblCarFlowLocation, blockName, lblfloor;
    Spinner spinner, spinner1;
    List<String> blockNameList, flooridList;
    TableLayout tableLayout;
    String pending = "PENDING";
    String floorName;
    int rowCount = 0;

    private OnFragmentInteractionListener mListener;

    public SuspiciousFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SuspiciousFragment newInstance(String param1, String param2) {
        SuspiciousFragment fragment = new SuspiciousFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public void addTableHeader() {
        tableLayout.removeAllViews();
        lbllicenseNo = new TextView(getContext());
        lblentryDateTime = new TextView(getContext());
        lblCarFlowLocation = new TextView(getContext());
        lbllicenseNo.setText("Vehicle");
        lblentryDateTime.setText("Entry Datetime");
        lblCarFlowLocation.setText("Flow Location");
        lbllicenseNo.setTextSize(18);
        lbllicenseNo.setGravity(Gravity.CENTER);
        lblentryDateTime.setTextSize(18);
        lblentryDateTime.setGravity(Gravity.CENTER);
        lblCarFlowLocation.setTextSize(18);
        lblCarFlowLocation.setGravity(Gravity.CENTER);
        lbllicenseNo.setPadding(10, 10, 10, 10);
        lblCarFlowLocation.setPadding(10, 10, 10, 10);
        lblentryDateTime.setPadding(10, 10, 10, 10);
        lbllicenseNo.setTextColor(Color.WHITE);
        lblentryDateTime.setTextColor(Color.WHITE);
        lblCarFlowLocation.setTextColor(Color.WHITE);
        tr = new TableRow(getContext());
        tr.addView(lbllicenseNo);
        tr.addView(lblentryDateTime);
        tr.addView(lblCarFlowLocation);
        tr.setBackgroundColor(Color.BLACK);
        tableLayout.addView(tr);
    }

    public void displayAllRecords(EntryRecords ent) {
        licenseNo = new TextView(getContext());
        entryDateTime = new TextView(getContext());
        CarFlowLocation = new TextView(getContext());
        licenseNo.setText(ent.getVehicleLicensePlateNumber());
        entryDateTime.setText(ent.getDate() + " " + ent.getTime());
        floorRef = FirebaseDatabase.getInstance().getReference("Floors").child(ent.getCarflowLocation());
        floorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                floorName = dataSnapshot.child("floorName").getValue().toString().toUpperCase();
                CarFlowLocation.setText(floorName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        CarFlowLocation.setText(floorName);
        licenseNo.setTextSize(15);
        licenseNo.setGravity(Gravity.CENTER);
        entryDateTime.setTextSize(15);
        entryDateTime.setGravity(Gravity.CENTER);
        CarFlowLocation.setTextSize(15);
        CarFlowLocation.setGravity(Gravity.CENTER);
        licenseNo.setPadding(30, 0, 30, 0);
        entryDateTime.setPadding(20, 0, 20, 0);
        CarFlowLocation.setPadding(5, 0, 5, 0);
        tr = new TableRow(getContext());
        tr.addView(licenseNo);
        tr.addView(entryDateTime);
        tr.addView(CarFlowLocation);
        tr.setBackgroundColor(Color.rgb(220, 220, 220));
        tableLayout.addView(tr);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suspicious, container, false);
        tableLayout = view.findViewById(R.id.table_layout);
        spinner = view.findViewById(R.id.dropdown);
        blockName = view.findViewById(R.id.block_name);

        blockNameList = new ArrayList<String>();
        flooridList = new ArrayList<String>();
        blockNameList.add("All");

        blockRef = FirebaseDatabase.getInstance().getReference("Blocks");
        entryRef = FirebaseDatabase.getInstance().getReference("EntryRecords");
        floor1Ref = FirebaseDatabase.getInstance().getReference("Floors");

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
                    blockName.setText("All Blocks");
                    addTableHeader();

                    entryRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot entry : dataSnapshot.getChildren()) {
                                EntryRecords ent = entry.getValue(EntryRecords.class);

                                if (ent.getStatus().toUpperCase().equals(pending) && ent.getParkingSlotNumber().equals("")) {
                                    String ed = ent.getDate();
                                    String et = ent.getTime();
                                    String combine = ed + " " + et;


                                    try {
                                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date entryd = sdfDate.parse(combine);

                                        Date currentd = new Date();

                                        long e = entryd.getTime();
                                        long c = currentd.getTime();

                                        long diff = c - e;

                                        long seconds = diff / 1000;
                                        final long minutes = seconds / 60;
                                        final long hours = minutes / 60;
                                        final long days = hours / 24;

                                        Log.wtf("days 123 : ", String.valueOf(days));
                                        Log.wtf("hours 123 : ", String.valueOf(hours));
                                        Log.wtf("minute 123: ", String.valueOf(minutes));

                                        if (days >= 1) {
                                            displayAllRecords(ent);
                                            rowCount++;
                                        } else if (hours >= 1) {
                                            displayAllRecords(ent);
                                            rowCount++;
                                        } else if (minutes >= 10) {
                                            displayAllRecords(ent);
                                            rowCount++;
                                        } else if (hours < 1) {
                                            if (minutes >= 10) {
                                                displayAllRecords(ent);
                                                rowCount++;
                                            }
                                        }

                                    } catch (ParseException e) {
                                        Log.d("hi", e.getMessage());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    tableLayout.removeAllViews();
                    blockName.setText("Block " + block_name);
                    addTableHeader();
                    blockRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Blocks blocks = data.getValue(Blocks.class);

                                if (blocks.getParkingLotid().equals("-LtmhqyC5ACsE-k8dB8Q") && block_name.equals(data.child("blockName").getValue())) {
                                    String bid = data.getKey();

                                    floor1Ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot floor : dataSnapshot.getChildren()) {
                                                Floors floors = floor.getValue(Floors.class);

                                                if (bid.equals(floors.getBlockid())) {
                                                    String floorName = floor.getKey();

                                                    entryRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot entry : dataSnapshot.getChildren()) {
                                                                EntryRecords ent = entry.getValue(EntryRecords.class);

                                                                if (ent.getStatus().toUpperCase().equals(pending) && ent.getParkingSlotNumber().equals("") && ent.carflowLocation.equals(floorName)) {
                                                                    String ed = ent.getDate();
                                                                    String et = ent.getTime();
                                                                    String combine = ed + " " + et;

                                                                    try {
                                                                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                        Date entryd = sdfDate.parse(combine);

                                                                        Date currentd = new Date();

                                                                        long e = entryd.getTime();
                                                                        long c = currentd.getTime();

                                                                        long diff = c - e;

                                                                        long seconds = diff / 1000;
                                                                        final long minutes = seconds / 60;
                                                                        final long hours = minutes / 60;
                                                                        final long days = hours / 24;

                                                                        if (days >= 1) {
                                                                            displayAllRecords(ent);
                                                                            rowCount++;
                                                                        } else if (hours >= 1) {
                                                                            displayAllRecords(ent);
                                                                            rowCount++;
                                                                        } else if (minutes >= 10) {
                                                                            displayAllRecords(ent);
                                                                            rowCount++;
                                                                        } else if (hours < 1) {
                                                                            if (minutes >= 10) {
                                                                                displayAllRecords(ent);
                                                                                rowCount++;
                                                                            }
                                                                        }

                                                                    } catch (ParseException e) {
                                                                        Log.d("hi", e.getMessage());
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
