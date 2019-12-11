package com.example.moonwaygravitystaff;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moonwaygravitystaff.Adapter.EntryRecordListAdapter;
import com.example.moonwaygravitystaff.Adapter.TransactionAdapter;
import com.example.moonwaygravitystaff.Model.EntryRecords;
import com.example.moonwaygravitystaff.Model.Transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewTransaction extends Fragment {


    private OnFragmentInteractionListener mListener;

    TransactionAdapter transAdapter;
    DatabaseReference transRef;
    List<Transaction> transactions;
    private RecyclerView transRecycler;


    public ViewTransaction() {
        // Required empty public constructor
    }
    public static ViewTransaction newInstance(String param1, String param2) {
        ViewTransaction fragment = new ViewTransaction();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_transaction, container, false);

        transRecycler = view.findViewById(R.id.entryRecycler);

        transactions = new ArrayList<>();

        transRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        transRecycler.setLayoutManager(linearLayoutManager);

        transRef = FirebaseDatabase.getInstance().getReference("Transaction");

        transRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Transaction transaction = data.getValue(Transaction.class);
                    transaction.setKey(data.getKey());
                    transactions.add(transaction);


                }
                transAdapter = new TransactionAdapter(getActivity(), transactions);
                transRecycler.setAdapter(transAdapter);



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
