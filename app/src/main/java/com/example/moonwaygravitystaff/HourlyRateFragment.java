package com.example.moonwaygravitystaff;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HourlyRateFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    DatabaseReference parkingLotRef;
    EditText edtHourly,edtDaily,edtFirstHour;
    Button btnModify,btnSubmit;

    public HourlyRateFragment() {
        // Required empty public constructor
    }


    public static HourlyRateFragment newInstance(String param1, String param2) {
        HourlyRateFragment fragment = new HourlyRateFragment();
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
        View view = inflater.inflate(R.layout.fragment_hourly_rate, container, false);

        edtHourly = view.findViewById(R.id.edtHourly);
        edtDaily = view.findViewById(R.id.edtDaily);
        edtFirstHour = view.findViewById(R.id.edtFirstHour);

        btnModify = view.findViewById(R.id.btnModify);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        parkingLotRef = FirebaseDatabase.getInstance().getReference("ParkingLots");

        parkingLotRef.child("-LtmhqyC5ACsE-k8dB8Q").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String hourlyRate = dataSnapshot.child("hourlyRate").getValue().toString();
                String firstHourRate = dataSnapshot.child("firstHourRate").getValue().toString();
                String dailyRate = dataSnapshot.child("dailyRate").getValue().toString();

                edtHourly.setText(hourlyRate);
                edtDaily.setText(dailyRate);
                edtFirstHour.setText(firstHourRate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtHourly.setEnabled(true);
                edtDaily.setEnabled(true);
                edtFirstHour.setEnabled(true);
                btnSubmit.setVisibility(View.VISIBLE);
                btnModify.setVisibility(View.GONE);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hour = edtHourly.getText().toString();
                String first = edtFirstHour.getText().toString();
                String daily = edtDaily.getText().toString();
                parkingLotRef.child("-LtmhqyC5ACsE-k8dB8Q").child("hourlyRate").setValue(hour);
                parkingLotRef.child("-LtmhqyC5ACsE-k8dB8Q").child("firstHourRate").setValue(first);
                parkingLotRef.child("-LtmhqyC5ACsE-k8dB8Q").child("dailyRate").setValue(daily);
                btnSubmit.setVisibility(View.GONE);
                btnModify.setVisibility(View.VISIBLE);
                edtHourly.setEnabled(false);
                edtDaily.setEnabled(false);
                edtFirstHour.setEnabled(false);

                showToast(R.drawable.success_60,"Updated");

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
    private void showToast(int pngpath, String text) {
        Toast toast = new Toast(getActivity());
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, pngpath, 0, 0);
        toast.setView(textView);
        toast.show();
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
