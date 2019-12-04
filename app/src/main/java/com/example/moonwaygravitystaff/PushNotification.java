package com.example.moonwaygravitystaff;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PushNotification extends Service {
    FirebaseUser user;
    DatabaseReference mDatabase,userRef;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null){
            userRef = FirebaseDatabase.getInstance().getReference("Staff");
            userRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String role = dataSnapshot.child("role").getValue().toString();
                    if(role.equals("staff")) {
                        mDatabase = FirebaseDatabase.getInstance().getReference("Notification");
                        mDatabase.addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            String status = data.child("status").getValue().toString();
                                            String userId = data.child("customerId").getValue().toString();
                                            String key = data.getKey();
                                            Log.wtf("hi123", user.getUid());
                                            if (status.equals("Inform Staff")) {
                                                Intent yesintent = new Intent(PushNotification.this, MainActivity.class);
                                                yesintent.putExtra("action", "yes");
                                                yesintent.putExtra("key", key);
                                                PendingIntent yespendingIntent =
                                                        PendingIntent.getBroadcast(PushNotification.this, 0, yesintent, PendingIntent.FLAG_UPDATE_CURRENT);


                                                Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(PushNotification.this)
                                                        .setSmallIcon(R.drawable.logo)
                                                        .setContentText("My Notification")
                                                        .setContentTitle("Car stolen by someone issue")
                                                        .setAutoCancel(true)
                                                        .setSound(defSoundUri)
                                                        .setContentIntent(yespendingIntent);
                                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                notificationManager.notify(2 /* ID of notification */, builder.build());


                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                }
                        );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }
    }






}
