package com.example.moonwaygravitystaff;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ManageAnnouncement extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    EditText announcementContent;
    Button btnModify;

    private OnFragmentInteractionListener mListener;

    public ManageAnnouncement() {
        // Required empty public constructor
    }
    public static ManageAnnouncement newInstance(String param1, String param2) {
        ManageAnnouncement fragment = new ManageAnnouncement();
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
        View view = inflater.inflate(R.layout.fragment_manage_announcement, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        btnModify = view.findViewById(R.id.btnModifyAnnouncement);
        announcementContent = view.findViewById(R.id.announcementContent);
        retrieveAnnouncement();
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = btnModify.getText().toString();
                if(btnText.toLowerCase().equals("modify")){
                    announcementContent.setEnabled(true);
                    btnModify.setText("Submit");
                    btnModify.setBackgroundColor(Color.GREEN);

                }else{
                    updateAnnouncement(announcementContent.getText().toString());
                    announcementContent.setEnabled(false);
                    btnModify.setText("Modify");
                    btnModify.setBackgroundColor(getResources().getColor(R.color.color_primary));
                }

            }
        });
    }
    private void showToast(int pngpath, String text) {
        Toast toast = new Toast(getActivity());
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, pngpath, 0, 0);
        toast.setView(textView);
        toast.show();
    }
    private void retrieveAnnouncement(){
        DatabaseReference announcementRef = database.getReference("Announcement");
        announcementRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    String msg = snapshot.child("message").getValue().toString();
                    if(msg != null){
                        announcementContent.setText(msg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateAnnouncement(String msg){
        DatabaseReference announcementRef = database.getReference("Announcement");

        announcementRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                   if(snapshot.hasChild("message")){
                       announcementRef.child(snapshot.getKey()).child("message").setValue(msg);
                       announcementRef.child(snapshot.getKey()).child("createdby").setValue(firebaseUser.getUid());
                       showToast(R.drawable.success_60,"Update successfully!");
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
