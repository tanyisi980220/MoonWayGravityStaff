package com.example.moonwaygravitystaff;

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
import android.view.View;
import android.view.ViewGroup;
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

public class CarFlowFragment extends Fragment{
    DatabaseReference blockRef, floorRef;
    TableRow tr;
    TextView floorName, parkedNo, flowNo;
    Spinner spinner;
    List<String> blockNameList;

    private OnFragmentInteractionListener mListener;

    public CarFlowFragment() {
        // Required empty public constructor
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
        View view =inflater.inflate(R.layout.fragment_car_flow, container, false);
        TableLayout tableLayout = view.findViewById(R.id.table_layout);
        spinner = view.findViewById(R.id.dropdown);

        blockNameList = new ArrayList<String>();

        blockRef = FirebaseDatabase.getInstance().getReference("Blocks");
        floorRef = FirebaseDatabase.getInstance().getReference("Floors");

        blockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Blocks blocks = data.getValue(Blocks.class);
                    if(blocks.getParkingLotid().equals("-LtmhqyC5ACsE-k8dB8Q")){

                        final String blockid = data.getKey();

                        blockNameList.add(data.child("blockName").getValue().toString());

                        floorRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot floor : dataSnapshot.getChildren()) {
                                    Floors floors = floor.getValue(Floors.class);
                                    if(blockid.equals(floors.getBlockid())) {
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
                                        tr.setBackgroundColor(Color.rgb(220,220,220));
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, blockNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        


        return view;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void addTableRow(Blocks blocks){
    }
}
