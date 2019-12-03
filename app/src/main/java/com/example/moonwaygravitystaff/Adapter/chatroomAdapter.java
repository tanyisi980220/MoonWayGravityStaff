package com.example.moonwaygravitystaff.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moonwaygravitystaff.Model.Chatroom;
import com.example.moonwaygravitystaff.Model.Customer;
import com.example.moonwaygravitystaff.R;
import com.example.moonwaygravitystaff.StaffChatMessaging;
import com.example.moonwaygravitystaff.drawerMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class chatroomAdapter extends RecyclerView.Adapter<chatroomAdapter.chatroomViewHolder> {
    private Context context;
    private List<Chatroom> chatrooms;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser firebaseUser;

    public chatroomAdapter(Context context, List<Chatroom> chatrooms) {
        this.chatrooms = chatrooms;
        this.context = context;
    }

    @NonNull
    @Override
    public chatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatroom_item, parent, false);
        return new chatroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final chatroomViewHolder holder, int position) {
        final Chatroom chatroom = chatrooms.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Date date = chatroom.getDate();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateOnly = formatter.format(date);
        holder.chatroomDate.setText(dateOnly);
        DatabaseReference customerRef = database.getReference("Customer");
        customerRef.child(chatroom.getCustomerid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer customer = dataSnapshot.getValue(Customer.class);
                holder.customerName.setText(customer.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("hmmmm", "yes");
                holder.mView.findViewById(R.id.chatroomItemLayout).setBackgroundResource(R.color.grey);
                //Update the staff id and jump to the activity
                DatabaseReference messageRef = database.getReference("Message");
                DatabaseReference chatroomRef = database.getReference("Chatroom");

                chatroomRef.child(chatroom.getId()).child("staffid").setValue(firebaseUser.getUid());
                Intent intent = new Intent(context, StaffChatMessaging.class);
                intent.putExtra("chatroomid",chatroom.getId());
                intent.putExtra("customerid",chatroom.getCustomerid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatrooms.size();
    }

    public class chatroomViewHolder extends RecyclerView.ViewHolder {
        TextView customerName;
        TextView chatroomDate;
        View mView;

        public chatroomViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            customerName = itemView.findViewById(R.id.customerName);
            chatroomDate = itemView.findViewById(R.id.chatroomDate);
        }
    }
}


