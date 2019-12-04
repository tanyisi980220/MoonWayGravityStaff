package com.example.moonwaygravitystaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class entryRecordDetail extends AppCompatActivity {

    DatabaseReference entryRef,exitRef;
    EditText edtLicensePlate;
    TextView txtEntryDate,txtEntryTime,txtExitDate,txtExitTime;
    Button btnModify,btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_record_detail);
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

        txtEntryDate = findViewById(R.id.txtEntryDate1);
        txtEntryTime = findViewById(R.id.txtEntryTime1);
        txtExitDate = findViewById(R.id.txtExitDate1);
        txtExitTime = findViewById(R.id.txtExitTime1);
        edtLicensePlate = findViewById(R.id.edtLicensePlate);

        btnModify = findViewById(R.id.btnModify);
        btnSubmit = findViewById(R.id.btnSubmit);

        Intent intent = getIntent();

        Log.wtf("entry123",intent.getStringExtra("vehicleLicensePlate"));

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtLicensePlate.setEnabled(true);
                btnSubmit.setVisibility(View.VISIBLE);
                btnModify.setVisibility(View.GONE);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(entryRecordDetail.this);
                String lp = edtLicensePlate.getText().toString();
                String dt = txtEntryDate.getText().toString();
                String tm = txtEntryTime.getText().toString();
                builder.setMessage("Are you sure you want to change the Vehicle License Plate Number to "+lp+ " ? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                entryRef = FirebaseDatabase.getInstance().getReference("EntryRecords");

                                entryRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot data:dataSnapshot.getChildren()){
                                            String date = data.child("date").getValue().toString();
                                            String time = data.child("time").getValue().toString();
                                            String licensePlate = data.child("vehicleLicensePlateNumber").getValue().toString();
                                            if(date.equals(dt) && time.equals(tm) && licensePlate.equals(intent.getStringExtra("vehicleLicensePlate"))){
                                                entryRef.child(data.getKey()).child("vehicleLicensePlateNumber").setValue(lp);
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                }).show();
            }
        });




        entryRef = FirebaseDatabase.getInstance().getReference("EntryRecords");

        entryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String date = data.child("date").getValue().toString();
                    String time = data.child("time").getValue().toString();
                    String licensePlate = data.child("vehicleLicensePlateNumber").getValue().toString();
                    if(date.equals(intent.getStringExtra("entryDate")) &&
                    time.equals(intent.getStringExtra("entryTime")) &&
                    licensePlate.equals(intent.getStringExtra("vehicleLicensePlate"))){
                        edtLicensePlate.setText(licensePlate);
                        txtEntryDate.setText(date);
                        txtEntryTime.setText(time);
                        searchExitRecord(data.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void searchExitRecord(String key){
        exitRef = FirebaseDatabase.getInstance().getReference("ExitRecords");
        exitRef.orderByChild("entryId").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    txtExitDate.setText(data.child("exitDate").getValue().toString());
                    txtExitTime.setText(data.child("exitTime").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
