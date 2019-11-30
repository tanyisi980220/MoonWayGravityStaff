package com.example.moonwaygravitystaff.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moonwaygravitystaff.Model.Message;
import com.example.moonwaygravitystaff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.ViewHolder> {
    public static final int msg_type_left = 0;
    public static final int msg_type_right = 1;
    private Context context;
    private List<Message> messages;

    FirebaseUser firebaseUser;

    public messageAdapter(Context context, List<Message> messages){
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public messageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == msg_type_right){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new messageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new messageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull messageAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.show_message.setText(message.getMessage());
        holder.profile_image.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(messages.get(position).getSender().equals(firebaseUser.getUid())){
            return msg_type_right;
        }else{
            return msg_type_left;
        }
    }
}
