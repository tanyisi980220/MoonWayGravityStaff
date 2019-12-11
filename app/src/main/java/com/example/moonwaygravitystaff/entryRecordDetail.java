package com.example.moonwaygravitystaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class entryRecordDetail extends AppCompatActivity {

    DatabaseReference entryRef,exitRef;
    EditText edtLicensePlate;
    TextView txtEntryDate,txtEntryTime,txtExitDate,txtExitTime,txtFees,txtUn;
    Button btnModify,btnSubmit;
    ImageView imgDriver,imgLicensePlate,imgUnauthorizedDriver;
    StorageReference storageRef,driverStorageRef,unauthorizeDriverStorageRef;

    private Animator currentAnimator;
    ImageView expandedImageView;
    private int shortAnimationDuration;

    Context context;

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
        txtFees = findViewById(R.id.txtFees1);
        txtUn = findViewById(R.id.txtUnauthorizedFaceImg);
        edtLicensePlate = findViewById(R.id.edtLicensePlate);

        imgDriver = findViewById(R.id.imgDriver);
        imgLicensePlate = findViewById(R.id.imgLicensePlate);
        imgUnauthorizedDriver = findViewById(R.id.imgUnauthorizedDriver);
        expandedImageView = findViewById(R.id.expanded_image);


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

                        FirebaseStorage storage= FirebaseStorage.getInstance();

                        String licensePlateUrl = data.child("licensePlateImage").getValue().toString();
                        imgLicensePlate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int check =2;
                                zoomImageFromThumb(imgDriver, licensePlateUrl,check);
                            }
                        });
                        storageRef = storage.getReference();
                        storageRef.child("VehicleLicensePlate/"+licensePlateUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(context).load(uri).into(imgLicensePlate);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                        String driverUrl = data.child("authorizeDriverImage").getValue().toString();
                        imgDriver.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int check =1;
                                zoomImageFromThumb(imgDriver, driverUrl,check);
                            }
                        });
                        driverStorageRef = storage.getReference();
                        storageRef.child("Face/"+driverUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(context).load(uri).into(imgDriver);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        if(data.child("unauthorizeDriverImage").getValue()!=null){
                            String unDriverUrl = data.child("unauthorizeDriverImage").getValue().toString();
                            txtUn.setVisibility(View.VISIBLE);
                            imgUnauthorizedDriver.setVisibility(View.VISIBLE);
                            imgUnauthorizedDriver.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int check =3;
                                    zoomImageFromThumb(imgDriver, unDriverUrl,check);
                                }
                            });
                            unauthorizeDriverStorageRef = storage.getReference();
                            storageRef.child("UnauthorizedDriver/"+unDriverUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.with(context).load(uri).into(imgUnauthorizedDriver);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }
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
                    txtFees.setText(data.child("fees").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void zoomImageFromThumb(final View thumbView, String imageResId,int check) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        if(check==1){
            FirebaseStorage storage= FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            storageRef.child("Face/"+imageResId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri).into(expandedImageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }else if(check==2){
            FirebaseStorage storage= FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            storageRef.child("VehicleLicensePlate/"+imageResId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri).into(expandedImageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }else{
            FirebaseStorage storage= FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            storageRef.child("UnauthorizedDriver/"+imageResId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri).into(expandedImageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }




        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);
        findViewById(R.id.layout1).setVisibility(View.VISIBLE);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        findViewById(R.id.layout1).setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }
}
