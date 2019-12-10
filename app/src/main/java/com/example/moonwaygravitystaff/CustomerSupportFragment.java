package com.example.moonwaygravitystaff;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moonwaygravitystaff.Adapter.chatroomAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moonwaygravitystaff.Model.Chatroom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class CustomerSupportFragment extends Fragment {
    private FirebaseUser firebaseUser;

    chatroomAdapter chatroomAdapter;
    List<Chatroom> chatrooms = new ArrayList<>();
    RecyclerView chatroomRecycleView;
    TextView NoChatroom;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Resources res;
    private OnFragmentInteractionListener mListener;

    public CustomerSupportFragment() {
        // Required empty public constructor
    }


    public static CustomerSupportFragment newInstance() {
        CustomerSupportFragment fragment = new CustomerSupportFragment();
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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_customer_support, container, false);
        init(view);

        return view;
    }

    private void init(View view) {
        NoChatroom = view.findViewById(R.id.noChatroomLabel);
        chatroomRecycleView = view.findViewById(R.id.chatroomRecycleView);
        chatroomRecycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chatroomRecycleView.setLayoutManager(linearLayoutManager);
        retrieveActiveChatroom();


    }

    private void retrieveActiveChatroom() {
        chatrooms.clear();
        final DatabaseReference chatroomRef = database.getReference("Chatroom");
        chatroomRef.orderByChild("status").equalTo(getString(R.string.ActiveState)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

                    Chatroom chatroom = dataSnapshot.getValue(Chatroom.class);
                    chatroom.setId(dataSnapshot.getKey());
                    if (chatroom.getStaffid().equals("") || chatroom.getStaffid().equals(firebaseUser.getUid())){
                        chatrooms.add(chatroom);
                        chatroomAdapter = new chatroomAdapter(getActivity(), chatrooms);
                        chatroomRecycleView.setAdapter(chatroomAdapter);
                    }

                    if (chatrooms.size()<= 0) {
                        NoChatroom.setVisibility(View.VISIBLE);
                    }else{
                        NoChatroom.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                    }
                },
                1200);

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
