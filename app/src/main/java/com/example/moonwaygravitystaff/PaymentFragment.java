package com.example.moonwaygravitystaff;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentFragment extends Fragment {




    private OnFragmentInteractionListener mListener;
    Button btnSearch,btnPay;
    TextInputEditText txtLicensePlate,edtCash;
    TextView txtParkingFee,txtDate,txtTime;
    DatabaseReference entryRef,parkingLotRef,transRef;
    Double fees;
    ProgressDialog dialog;
    LinearLayout layout;

    public PaymentFragment() {
        // Required empty public constructor
    }

    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();
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
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        btnSearch = view.findViewById(R.id.btnSearch);
        txtLicensePlate = view.findViewById(R.id.txtLicensePlate);
        txtParkingFee = view.findViewById(R.id.txtParkingFee);
        btnPay = view.findViewById(R.id.btnPay);
        txtDate = view.findViewById(R.id.txtDate);
        txtTime = view.findViewById(R.id.txtTime);
        edtCash = view.findViewById(R.id.edtCash);
        layout = view.findViewById(R.id.layout1);

        if(layout.getParent()!=null){
            ((ViewGroup)layout.getParent()).removeView(layout);
        }

        dialog = new ProgressDialog(getActivity(),ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        dialog.setTitle("Processing");
        dialog.setMessage("Please wait...");



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                entryRef = FirebaseDatabase.getInstance().getReference("EntryRecords");
                entryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int check = 0;
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            String licensePlate = data.child("vehicleLicensePlateNumber").getValue().toString();
                            String txtPlate = txtLicensePlate.getText().toString();
                            if(licensePlate.toUpperCase().equals(txtPlate.toUpperCase()) && data.child("status").getValue().equals("Pending")){
                                showToast(R.drawable.success_60,"Record founded");
                                String date = data.child("date").getValue().toString();
                                String time = data.child("time").getValue().toString();
                                txtParkingFee.setVisibility(View.VISIBLE);
                                txtDate.setVisibility(View.VISIBLE);
                                txtTime.setVisibility(View.VISIBLE);
                                btnPay.setVisibility(View.VISIBLE);
                                txtDate.setText("Entry Date : "+date);
                                txtTime.setText("Entry Date : "+time);

                                calculateFees(date,time,licensePlate,data.getKey());
                                check =1;
                            }


                        }
                        if(check < 1){
                            showToast(R.drawable.no,"No Record founded");
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        return view;
    }
    private void calculateFees(final String date,final String time,final String licensePlateNumber,final String key) {
        String ed = date;
        String et = time;
        String combine = ed + " " + et;

        if(layout.getParent()!=null){
            ((ViewGroup)layout.getParent()).removeView(layout);
        }


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

            parkingLotRef = FirebaseDatabase.getInstance().getReference("ParkingLots").child("-LtmhqyC5ACsE-k8dB8Q");
            parkingLotRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final int dailyRate = Integer.parseInt(dataSnapshot.child("dailyRate").getValue().toString());
                    int hourlyRate = Integer.parseInt(dataSnapshot.child("hourlyRate").getValue().toString());
                    int firstHourRate = Integer.parseInt(dataSnapshot.child("firstHourRate").getValue().toString());
                    if (hours >= 24) {
                        fees = (double) days * dailyRate;
                    } else if (minutes <= 20) {
                        fees = 0.00;
                    } else if (minutes > 20 && hours < 24) {
                        fees = (double) firstHourRate + (hours * hourlyRate);
                    } else if (hours == 1 && minutes > 20) {
                        fees = (double) firstHourRate;
                    }
                    String parkingFee = String.format("%.2f", fees);
                    txtParkingFee.setText("Parking Fee : RM" +parkingFee);

                    dialog.dismiss();
                    payFees(fees,date,time,licensePlateNumber,key);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (ParseException e) {
            Log.d("hi", e.getMessage());
        }
    }

    public void payFees(final double fees,final String date,final String time,final String licensePlate,final String key){
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                builder.setView(layout)
                        .setIcon(R.drawable.success_60)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String c = edtCash.getText().toString();
                                if(!c.isEmpty()){
                                    double cash = Double.parseDouble(c);
                                    double change = cash - fees;
                                    if(change<0){
                                        showToast(R.drawable.no,"The cash amount is not enough");
                                        calculateFees(date,time,licensePlate,key);
                                    }else{
                                        Date date = Calendar.getInstance().getTime();
                                        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
                                        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

                                        transRef = FirebaseDatabase.getInstance().getReference();
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("status", "Approve");
                                        hashMap.put("amount", fees);
                                        hashMap.put("transactionType", "Pay Parking Fees");
                                        hashMap.put("transactionDate", sdfDate.format(date));
                                        hashMap.put("transactionTime", sdfTime.format(date));
                                        
                                        String id = sdfDate.format(date)+sdfTime.format(date)+key;

                                        transRef.child("Transaction").child(id).setValue(hashMap);

                                        showToast(R.drawable.success_60,"Parking fee paid");
                                        entryRef.child(key).child("status").setValue("Paid");
                                        entryRef.child(key).child("transID").setValue(id);
                                        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                                        builder.setMessage("Change : RM"+ change +"\nPlease exit within 20 minutes")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        txtParkingFee.setVisibility(View.INVISIBLE);
                                                        txtDate.setVisibility(View.INVISIBLE);
                                                        txtTime.setVisibility(View.INVISIBLE);
                                                        btnPay.setVisibility(View.INVISIBLE);
                                                        txtLicensePlate.setText("");

                                                    }
                                                }).show();
                                    }

                                }


                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                calculateFees(date,time,licensePlate,key);
                                dialogInterface.cancel();

                        }
                    }).show();


            }
        });
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
