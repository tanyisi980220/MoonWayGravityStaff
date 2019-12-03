package com.example.moonwaygravitystaff;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moonwaygravitystaff.Adapter.EntryRecordListAdapter;
import com.example.moonwaygravitystaff.Model.EntryRecords;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EntryRecordMenuFragment extends Fragment {


    private Animator currentAnimator;
    ImageView expandedImageView;


    private int shortAnimationDuration;
    EntryRecordListAdapter entryAdapter;
    DatabaseReference entryRef;
    Context context;
    List<EntryRecords> entryRec;
    private RecyclerView entryRecycler;
    StorageReference storageRef;
    Spinner spinner;
    TextInputEditText edtDate,edtVehicle,edtDateTime;

    private OnFragmentInteractionListener mListener;

    public EntryRecordMenuFragment() {
        // Required empty public constructor
    }


    public static EntryRecordMenuFragment newInstance(String param1, String param2) {
        EntryRecordMenuFragment fragment = new EntryRecordMenuFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entry_record_menu, container, false);
        expandedImageView = (ImageView) view.findViewById(
                R.id.expanded_image);
        entryRecycler = view.findViewById(R.id.entryRecycler);

        spinner = view.findViewById(R.id.dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        edtDate = view.findViewById(R.id.edtDate);
        edtVehicle = view.findViewById(R.id.edtVehicle);
        edtDateTime = view.findViewById(R.id.edtDateTime);

        entryRec = new ArrayList<>();

        entryRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        entryRecycler.setLayoutManager(linearLayoutManager);

        entryRef = FirebaseDatabase.getInstance().getReference("EntryRecords");

        edtDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String date = edtDate.getText().toString();
                Log.wtf("hi123",date);
                entryRecycler.removeAllViewsInLayout();
                entryRec.clear();
                entryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            EntryRecords entryRecords = data.getValue(EntryRecords.class);
                            if(entryRecords.getDate().equals(date)){
                                entryRec.add(entryRecords);
                            }
                        }
                        entryAdapter = new EntryRecordListAdapter(getActivity(), entryRec);
                        entryRecycler.setAdapter(entryAdapter);


                        entryAdapter.onBind = (viewHolder, position, ent) -> {
                            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    zoomImageFromThumb(viewHolder.image, ent.getLicensePlateImage());
                                }
                            });
                        };
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        edtVehicle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String licensePlate = edtVehicle.getText().toString();
                Log.wtf("hi123",licensePlate);
                entryRecycler.removeAllViewsInLayout();
                entryRec.clear();

                entryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int check=0;
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            EntryRecords entryRecords = data.getValue(EntryRecords.class);
                            if(entryRecords.getVehicleLicensePlateNumber().toUpperCase().contains(licensePlate.toUpperCase().trim())){
                                entryRec.add(entryRecords);
                                check ++;
                            }
                        }
                        if(check==0){
                            showToast(R.drawable.no,"No Record Founded");
                        }
                        entryAdapter = new EntryRecordListAdapter(getActivity(), entryRec);
                        entryRecycler.setAdapter(entryAdapter);

                        entryAdapter.onBind = (viewHolder, position, ent) -> {
                            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    zoomImageFromThumb(viewHolder.image, ent.getLicensePlateImage());
                                }
                            });
                        };
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                String date = "date";
                String licensePlate = "vehicle License Plate number";
                String dateAndTime = "date and time";
                if(item.toUpperCase().trim().equals(date.toUpperCase())){
                    edtDate.setVisibility(View.VISIBLE);
                    edtVehicle.setVisibility(View.GONE);
                    edtDateTime.setVisibility(View.GONE);
                    edtDate.setText("");
                    edtVehicle.setText("");
                    edtDateTime.setText("");
                    createDateDialog();

                }else if(item.toUpperCase().trim().equals(licensePlate.toUpperCase())){
                    edtVehicle.setVisibility(View.VISIBLE);
                    edtDateTime.setVisibility(View.GONE);
                    edtDate.setVisibility(View.GONE);
                    edtDate.setText("");
                    edtVehicle.setText("");
                    edtDateTime.setText("");
                }else if(item.toUpperCase().trim().equals(dateAndTime.toUpperCase())){
                    edtDateTime.setVisibility(View.VISIBLE);
                    edtDate.setVisibility(View.GONE);
                    edtVehicle.setVisibility(View.GONE);
                    edtDate.setText("");
                    edtVehicle.setText("");
                    edtDateTime.setText("");
                }else{
                    edtDate.setVisibility(View.GONE);
                    edtVehicle.setVisibility(View.GONE);
                    edtDateTime.setVisibility(View.GONE);
                    edtDate.setText("");
                    edtVehicle.setText("");
                    edtDateTime.setText("");

                    createEntry();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });










        return view;


    }
    public void createDateDialog(){
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                edtDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date1,myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    public void createEntry(){
        entryRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                entryRecycler.removeAllViewsInLayout();
                entryRec.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EntryRecords entryRecords = snapshot.getValue(EntryRecords.class);
                    entryRec.add(entryRecords);


                }
                entryAdapter = new EntryRecordListAdapter(getActivity(), entryRec);
                entryRecycler.setAdapter(entryAdapter);

                entryAdapter.onBind = (viewHolder, position, ent) -> {
                    viewHolder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            zoomImageFromThumb(viewHolder.image, ent.getLicensePlateImage());
                        }
                    });
                };
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private void zoomImageFromThumb(final View thumbView, String imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        FirebaseStorage storage= FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        storageRef.child("VehicleLicensePlate/"+imageResId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri).into(expandedImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



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
        getActivity().findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
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
        getActivity().findViewById(R.id.layout1).setVisibility(View.VISIBLE);

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
                        getActivity().findViewById(R.id.layout1).setVisibility(View.GONE);
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


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showToast(int pngpath, String text) {
        Toast toast = new Toast(getActivity());
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, pngpath, 0, 0);
        toast.setView(textView);
        toast.show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
