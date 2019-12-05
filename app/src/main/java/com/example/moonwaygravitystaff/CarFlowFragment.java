package com.example.moonwaygravitystaff;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.moonwaygravitystaff.Adapter.blockAdapter;
import com.example.moonwaygravitystaff.Model.Blocks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CarFlowFragment extends Fragment{
    DatabaseReference blockRef;
    List<Blocks> blocksList;
    blockAdapter bAdapter;
    private RecyclerView blockRecycle;

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

        blocksList = new ArrayList<>();
        blockRecycle = view.findViewById(R.id.blockRecycler);

        blockRecycle.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        blockRecycle.setLayoutManager(linearLayoutManager);

        blockRef = FirebaseDatabase.getInstance().getReference("Blocks");

        blockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Blocks blocks = data.getValue(Blocks.class);
                    if(blocks.getParkingLotid().equals("-LtmhqyC5ACsE-k8dB8Q")){
                        blocksList.add(blocks);
                    }
                }
                bAdapter = new blockAdapter(getActivity(), blocksList);
                blockRecycle.setAdapter(bAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
