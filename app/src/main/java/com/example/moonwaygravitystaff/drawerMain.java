package com.example.moonwaygravitystaff;

import android.content.Intent;
import android.os.Bundle;

import com.example.moonwaygravitystaff.Model.Staff;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class drawerMain extends AppCompatActivity {
    TextView textViewStaffEmail,textViewRole;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    MenuItem logout;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);

        Intent intent = new Intent(this, PushNotification.class);
        startService(intent);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();


        DatabaseReference staffRef = firebaseDatabase.getReference("Staff");
        staffRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Staff staff = dataSnapshot.getValue(Staff.class);
                    String role = staff.getRole();
                    init(role);//initialize
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void init(String role){


        DatabaseReference staffRef = firebaseDatabase.getReference("Staff");
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        //for navigation view set up
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        textViewRole =(TextView) navigationView.getHeaderView(0).findViewById(R.id.roleLabel);
        //set role text
        if(role.equals(getString(R.string.adminState))){
            textViewRole.setText("Admin");
        }else if(role.equals(getString(R.string.staffState))){
            textViewRole.setText("Staff");
        }

        textViewStaffEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.staffName);
        textViewStaffEmail.setText(firebaseUser.getEmail());

        logout = (MenuItem)navigationView.getMenu().findItem(R.id.nav_logout);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                auth.signOut();
                showToast(0,"You have log out.");
                startActivity(new Intent(drawerMain.this,StaffLogin.class));
                return true;
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_carflow, R.id.nav_suspicous,R.id.nav_improper,
                R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    private void checkUserLogin(){
        if(auth==null){

        }
    }
    private void showToast(int pngpath, String text) {
        Toast toast = new Toast(drawerMain.this);
        TextView textView = new TextView(drawerMain.this);
        textView.setText(text);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, pngpath, 0, 0);
        toast.setView(textView);
        toast.show();
    }
}
