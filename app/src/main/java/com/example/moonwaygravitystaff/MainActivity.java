package com.example.moonwaygravitystaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moonwaygravitystaff.Model.Floors;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private RecyclerView floorList;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Floors");
        databaseReference.keepSynced(true);

        floorList = (RecyclerView)findViewById(R.id.recycle_view);
        floorList.setHasFixedSize(true);
        floorList.setLayoutManager(new LinearLayoutManager(this));

        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(R.string.car_flow);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.car_flow:
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, StaffAuthenticationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.illegal_activity:
                        Toast.makeText(MainActivity.this, "My Cart", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Floors, FloorViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Floors, FloorViewHolder>
                (Floors.class, R.layout.floor_row, FloorViewHolder.class, databaseReference){
            @Override
            protected void populateViewHolder(FloorViewHolder floorViewHolder, Floors floors, int i) {
                floorViewHolder.setFloor_name(floors.getFloorName());
                Toast.makeText(MainActivity.this,floors.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        floorList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FloorViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public FloorViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setFloor_name(String floorName) {
            TextView fname = (TextView)mView.findViewById(R.id.floor_name);
            fname.setText(floorName);
        }


    }
}
