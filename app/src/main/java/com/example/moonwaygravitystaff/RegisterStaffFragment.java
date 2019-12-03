package com.example.moonwaygravitystaff;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moonwaygravitystaff.Model.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class RegisterStaffFragment extends Fragment {

    EditText name, password, confirmPassward, email;
    Spinner roleSpinner;
    Button submit;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    private OnFragmentInteractionListener mListener;

    public RegisterStaffFragment() {
        // Required empty public constructor
    }


    public static RegisterStaffFragment newInstance(String param1, String param2) {
        RegisterStaffFragment fragment = new RegisterStaffFragment();
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
        View view = inflater.inflate(R.layout.fragment_register_staff, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        Intent intent = getActivity().getIntent();
        String role = intent.getExtras().getString("role");


        name = view.findViewById(R.id.registerName);
        email = view.findViewById(R.id.registerEmail);
        password = view.findViewById(R.id.password);
        confirmPassward = view.findViewById(R.id.confirmPassword);
        roleSpinner = view.findViewById(R.id.roleSpinner);
        submit = view.findViewById(R.id.submitButton);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //initialize role dropdown
        ArrayList<String> roleSelection = new ArrayList<String>();
        roleSelection.add(getString(R.string.adminRole));
        roleSelection.add(getString(R.string.staffRole));
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, roleSelection);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        //initialize submit button click
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sname = name.getText().toString();
                String semail = email.getText().toString();
                String spassword = password.getText().toString();
                String sconfirm = confirmPassward.getText().toString();
                String srole = roleSpinner.getSelectedItem().toString();
                if (sname.equals("") || semail.equals("") || spassword.equals("") || sconfirm.equals("")) { // check blank
                    showToast(R.drawable.no, "Do not leave blank on any field!");
                } else {
                    String message = "";
                    if (!isNameFormat(sname.trim())) {
                        showToast(R.drawable.no, "Name should not contain digit \n and whitespace at end or start");
                    } else if (!spassword.equals(sconfirm)) {
                        showToast(R.drawable.no, "Password not match!");
                    } else {
                        registerNewStaff(sname, semail, spassword, srole);
                    }
                }
            }
        });
    }

    //Register new staff
    private void registerNewStaff(final String sname, String semail, String spassword, final String srole) {
        auth.createUserWithEmailAndPassword(semail, spassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DatabaseReference staffRef = firebaseDatabase.getReference("Staff");
                    Staff staff = new Staff();
                    staff.setName(sname);
                    staff.setRole(srole);

                    staffRef.child(firebaseUser.getUid()).setValue(staff);
                    showToast(R.drawable.success_60, "New Staff register Successfully");
                    Intent intent = getActivity().getIntent();
                    String p = intent.getExtras().getString("p");
                    String e = intent.getExtras().getString("e");
                    auth.signInWithEmailAndPassword(e,p);

                    resetAllField();
                }else{
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Log.d( "hi","Failed Registration: "+e.getMessage());
                    showToast(R.drawable.no,"This email already exist.");
                }
            }
        });
    }

    private void resetAllField() {
        name.setText("");
        email.setText("");
        password.setText("");
        confirmPassward.setText("");
    }

    private void showToast(int pngpath, String text) {
        Toast toast = new Toast(getActivity().getApplicationContext());
        TextView textView = new TextView(getActivity().getApplicationContext());
        textView.setText(text);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, pngpath, 0, 0);
        toast.setView(textView);
        toast.show();
    }

    private boolean isNameFormat(String staffname) {
        Pattern n = Pattern.compile("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$");
        if (!staffname.equals("")) {
            return n.matcher(staffname).matches();
        } else {
            return false;
        }

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
