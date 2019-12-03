package com.example.moonwaygravitystaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class StaffLogin extends AppCompatActivity {
    TextInputEditText loginEmail, loginPassword;
    Button btn_login;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    ProgressDialog dialog;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //check if user is not null
        if (firebaseUser != null) {
            proceedToMain();
        }

    }
    private void proceedToMain(){
        Intent intent = new Intent(this, drawerMain.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        init();
        database = FirebaseDatabase.getInstance();
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // get the login details
                String txt_email = loginEmail.getText().toString();
                String txt_pass = loginPassword.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)) { // check if anything in the field
                    showToast(R.drawable.no, "Please do not leave blank!!");
                } else {
                    dialog.show();

                    auth.signInWithEmailAndPassword(txt_email, txt_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference staffRef = database.getReference("Staff");
                                staffRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            dialog.dismiss();
                                            showToast(R.drawable.success_60, "Login Sucessfully!!");
                                            proceedToMain();


                                        }else{
                                            auth.signOut();
                                            showToast(R.drawable.no,"Authentication Failed");
                                            dialog.dismiss();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                showToast(R.drawable.no,"Authentication Failed");
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showToast(int pngpath, String text) {
        Toast toast = new Toast(StaffLogin.this);
        TextView textView = new TextView(StaffLogin.this);
        textView.setText(text);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, pngpath, 0, 0);
        toast.setView(textView);
        toast.show();
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById((R.id.loginPassword));
        btn_login = findViewById(R.id.loginButton);

        //init the progreess dialog
        dialog = new ProgressDialog(StaffLogin.this, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        dialog.setTitle("Logging In");
        dialog.setMessage("Please wait...");
    }
}
