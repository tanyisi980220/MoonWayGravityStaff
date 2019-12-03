package com.example.moonwaygravitystaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moonwaygravitystaff.StaffLogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moonwaygravitystaff.Adapter.messageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.moonwaygravitystaff.Model.Message;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StaffChatMessaging extends AppCompatActivity {
    RecyclerView recyclerViewMessage;
    ImageButton btnSend;
    Button btnEnd;
    EditText textSend;
    messageAdapter messageAdapter;
    List<Message> messages;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_chat_messaging);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String chatroomid = bundle.getString("chatroomid");
        final String customerid = bundle.getString("customerid");

        checkChatroomExistence(chatroomid);
        init(chatroomid);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // for send message button
                Date date = new Date();
                String msg = textSend.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(chatroomid, firebaseUser.getUid(), customerid, msg, date);
                    readMessage(chatroomid);
                    textSend.setText("");
                } else {
                    showToast(R.drawable.no, "Please enter your enquiries.");
                }
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // for send
                AlertDialog.Builder builder = new AlertDialog.Builder(StaffChatMessaging.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference chatroomRef = database.getReference("Chatroom");
                        chatroomRef.child(chatroomid).child("status").setValue(getString(R.string.CloseState));
                        Intent intent = new Intent(StaffChatMessaging.this,drawerMain.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }
    private void init(String chatroomid){
        readMessage(chatroomid);
        btnSend = findViewById(R.id.btn_send);
        textSend = findViewById(R.id.text_send);
        recyclerViewMessage = findViewById(R.id.recycleViewMessage);
        btnEnd = findViewById(R.id.btnEndConversation);
        messages = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewMessage.setLayoutManager(linearLayoutManager);
    }
    private void sendMessage(String chatroomid, String sender, String receiver, String message, Date date) { // push to firebase for message
        DatabaseReference messageRef = database.getReference("Message");
        com.example.moonwaygravitystaff.Model.Message msg = new Message();
        msg.setChatroomid(chatroomid);
        msg.setDate(date);
        msg.setReceiver(receiver);
        msg.setSender(sender);
        msg.setMessage(message);
        messageRef.push().setValue(msg);
    }

    private void readMessage(String chatroomid) {
        DatabaseReference messageRef = database.getReference("Message");
        messageRef.orderByChild("chatroomid").equalTo(chatroomid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    messages.add(message);

                }
                messageAdapter = new messageAdapter(StaffChatMessaging.this , messages);
                recyclerViewMessage.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showToast(int pngpath, String text) {
        Toast toast = new Toast(StaffChatMessaging.this);
        TextView textView = new TextView(StaffChatMessaging.this);
        textView.setText(text);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, pngpath, 0, 0);
        toast.setView(textView);
        toast.show();
    }

    private void checkChatroomExistence(String chatroomid) { // check the chatroom id can retrieve from previous page
        if (chatroomid.equals("")) {
            Toast.makeText(StaffChatMessaging.this, "Error occurred retrieving chatroom ID.", Toast.LENGTH_LONG);
            finish();
        }
    }
}
